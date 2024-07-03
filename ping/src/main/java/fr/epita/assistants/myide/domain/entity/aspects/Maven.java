package fr.epita.assistants.myide.domain.entity.aspects;

import fr.epita.assistants.myide.domain.entity.Aspect;
import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.features.maven.*;
import fr.epita.assistants.myide.domain.entity.features.maven.Package;

import java.util.ArrayList;
import java.util.List;

public class Maven implements Aspect {
    @Override
    public Type getType() {
        return Mandatory.Aspects.MAVEN;
    }

    @Override
    public List<Feature> getFeatureList() {
        return new ArrayList<Feature>() {{
            add(new Compile());
            add(new Clean());
            add(new Test());
            add(new Package());
            add(new Install());
            add(new Exec());
            add(new Tree());
        }};
    }
}
