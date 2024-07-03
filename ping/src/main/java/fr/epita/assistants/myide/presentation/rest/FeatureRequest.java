package fr.epita.assistants.myide.presentation.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeatureRequest
{
    private String feature;
    private List<String> params;
    private String project;
}
