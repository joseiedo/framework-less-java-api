package com.rest.app.config;

import com.rest.app.utils.Component;
import com.rest.app.utils.InjectionUtil;
import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.classes.ClassCriteria;
import org.burningwave.core.classes.ClassHunter;
import org.burningwave.core.classes.SearchConfig;

import javax.management.RuntimeErrorException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.burningwave.core.classes.ClassHunter.SearchResult;
import static org.burningwave.core.classes.SearchConfig.forResources;


/**
 * Injector, to create objects for all @CustomService classes. autowire/inject
 * all dependencies
 * <a href="https://dev.to/jjbrt/how-to-create-your-own-dependency-injection-framework-in-java-4eaj">Source</a>
 */
public class Injector {
    private Map<Class<?>, Class<?>> diMap;
    private final Map<Class<?>, Object> applicationScope;

    private static final Logger logger = Logger.getLogger(Injector.class.getName());

    private static Injector injector;

    private Injector() {
        super();
        diMap = new HashMap<>();
        applicationScope = new HashMap<>();
    }

    public static void startApplication(Class<?> mainClass) {
        try {
            synchronized (Injector.class) {
                if (injector == null) {
                    injector = new Injector();
                    injector.initFramework(mainClass);
                }
            }
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error occurred while injecting classes", ex);
        }
    }

    public static <T> T getService(Class<T> cls) {
        try {
            return injector.getBeanInstance(cls);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred while getting service instance", e);
        }
        return null;
    }

    /**
     * initialize the injector framework
     */
    private void initFramework(Class<?> mainClass)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, NoSuchMethodException, InvocationTargetException {

        // Set up the class hunter
        Class<?>[] classes = getClasses(mainClass.getPackage().getName(), true);
        ClassHunter classHunter = ComponentContainer.getInstance().getClassHunter();
        String packageRelPath = mainClass.getPackage().getName().replace(".", "/");

        // Get all classes with @Component annotation
        SearchResult components = classHunter.findBy(
                forResources(packageRelPath).by(
                        ClassCriteria.create().allThoseThatMatch(cls -> cls.getAnnotation(Component.class) != null)
                )
        );

        Collection<Class<?>> implementationClasses = components.getClasses();
        updateDIMapWithImplementations(implementationClasses);
        createAndStoreInstancesInContext(classes);
    }

    /**
     * Updates the {@code diMap} by grouping the implementation classes by the interfaces they implement.
     * Each implementation class is mapped to its implemented interfaces.
     * If an implementation class does not implement any interface, it is mapped to itself.
     *
     * @param classes A collection of classes that are to be grouped by their implemented interfaces.
     */
    public void updateDIMapWithImplementations(Collection<Class<?>> classes) {
        classes.forEach(implementationClass -> {
            Class<?>[] interfaces = implementationClass.getInterfaces();
            if (interfaces.length == 0) {
                diMap.put(implementationClass, implementationClass);
            } else {
                Arrays.stream(interfaces).forEach(interfaceClass -> diMap.put(implementationClass, interfaceClass));
            }
        });
    }


    /**
     * Fill the application scope with the instances of the classes with @Component annotation
     */
    public void createAndStoreInstancesInContext(Class<?>[] classes) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        for (Class<?> cls : classes) {
            if (cls.isAnnotationPresent(Component.class)) {
                Object classInstance = cls.getDeclaredConstructor().newInstance();
                applicationScope.put(cls, classInstance);
                InjectionUtil.autowire(this, cls, classInstance);
            }
        }
    }

    /**
     * Get all the classes for the input package
     */
    public Class<?>[] getClasses(String packageName, boolean recursive) {
        ComponentContainer componentConatiner = ComponentContainer.getInstance();
        ClassHunter classHunter = componentConatiner.getClassHunter();
        String packageRelPath = packageName.replace(".", "/");
        SearchConfig config = forResources(
                packageRelPath
        );
        if (!recursive) {
            config.findInChildren();
        }

        try (SearchResult result = classHunter.findBy(config)) {
            Collection<Class<?>> classes = result.getClasses();
            return classes.toArray(new Class[classes.size()]);
        }
    }


    /**
     * Create and Get the Object instance of the implementation class for input
     * interface service
     */
    @SuppressWarnings("unchecked")
    private <T> T getBeanInstance(Class<T> interfaceClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return (T) getBeanInstance(interfaceClass, null, null);
    }

    /**
     * Overload getBeanInstance to handle qualifier and autowire by type
     */
    public <T> Object getBeanInstance(Class<T> interfaceClass, String fieldName, String qualifier)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<?> implementationClass = getImplimentationClass(interfaceClass, fieldName, qualifier);

        if (applicationScope.containsKey(implementationClass)) {
            return applicationScope.get(implementationClass);
        }

        synchronized (applicationScope) {
            Object service = implementationClass.getDeclaredConstructor().newInstance();
            applicationScope.put(implementationClass, service);
            return service;
        }
    }

    /**
     * Get the name of the implimentation class for input interface service
     */
    private Class<?> getImplimentationClass(Class<?> interfaceClass, final String fieldName, final String qualifier) {
        Set<Entry<Class<?>, Class<?>>> implementationClasses = diMap.entrySet().stream()
                .filter(entry -> entry.getValue() == interfaceClass).collect(Collectors.toSet());
        String errorMessage = "";
        if (implementationClasses == null || implementationClasses.size() == 0) {
            errorMessage = "no implementation found for interface " + interfaceClass.getName();
        } else if (implementationClasses.size() == 1) {
            Optional<Entry<Class<?>, Class<?>>> optional = implementationClasses.stream().findFirst();
            if (optional.isPresent()) {
                return optional.get().getKey();
            }
        } else if (implementationClasses.size() > 1) {
            final String findBy = (qualifier == null || qualifier.trim().length() == 0) ? fieldName : qualifier;
            Optional<Entry<Class<?>, Class<?>>> optional = implementationClasses.stream()
                    .filter(entry -> entry.getKey().getSimpleName().equalsIgnoreCase(findBy)).findAny();
            if (optional.isPresent()) {
                return optional.get().getKey();
            } else {
                errorMessage = "There are " + implementationClasses.size() + " of interface " + interfaceClass.getName()
                        + " Expected single implementation or make use of @CustomQualifier to resolve conflict";
            }
        }
        throw new RuntimeErrorException(new Error(errorMessage));
    }
}
