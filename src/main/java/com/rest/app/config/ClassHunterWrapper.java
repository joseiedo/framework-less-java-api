package com.rest.app.config;

import com.rest.app.Application;
import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.classes.ClassCriteria;
import org.burningwave.core.classes.ClassHunter;
import org.burningwave.core.classes.ClassHunter.SearchResult;
import org.burningwave.core.classes.SearchConfig;

import java.util.Collection;
import java.util.function.Predicate;

public class ClassHunterWrapper {
    private final ClassHunter classHunter;
    private final String packageRelPath;

    public ClassHunterWrapper() {
        this.packageRelPath = Application.class.getPackage().getName().replace(".", "/");
        this.classHunter = ComponentContainer.getInstance().getClassHunter();
    }

    public Collection<Class<?>> findAllThatMatch(final Predicate<Class<?>> predicate) {
        return searchFor(ClassCriteria.create().allThoseThatMatch(predicate)).getClasses();
    }

    private SearchResult searchFor(ClassCriteria criteria) {
        return classHunter.findBy(SearchConfig.forResources(packageRelPath).by(criteria));
    }
}
