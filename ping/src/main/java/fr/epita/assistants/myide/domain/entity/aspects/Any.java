package fr.epita.assistants.myide.domain.entity.aspects;

import fr.epita.assistants.myide.domain.entity.Aspect;
import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.features.any.*;

import java.util.ArrayList;
import java.util.List;

public class Any implements Aspect {
    @Override
    public Type getType() {
        return Mandatory.Aspects.ANY;
    }

    @Override
    public List<Feature> getFeatureList() {
        return new ArrayList<Feature>() {{
            add(new Cleanup());
            add(new Dist());
            add(new Search());
        }};
    }
}
