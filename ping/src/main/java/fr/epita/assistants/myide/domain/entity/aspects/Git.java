package fr.epita.assistants.myide.domain.entity.aspects;

import fr.epita.assistants.myide.domain.entity.Aspect;
import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.features.git.Add;
import fr.epita.assistants.myide.domain.entity.features.git.Commit;
import fr.epita.assistants.myide.domain.entity.features.git.Pull;
import fr.epita.assistants.myide.domain.entity.features.git.Push;

import java.util.ArrayList;
import java.util.List;

public class Git implements Aspect {
    @Override
    public Type getType() {
        return Mandatory.Aspects.GIT;
    }

    @Override
    public List<Feature> getFeatureList() {
        return new ArrayList<Feature>() {{
                add(new Pull());
                add(new Add());
                add(new Push());
                add(new Commit());
            }};
    }
}
