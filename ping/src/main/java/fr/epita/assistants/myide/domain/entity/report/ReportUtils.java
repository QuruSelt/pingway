package fr.epita.assistants.myide.domain.entity.report;

import fr.epita.assistants.myide.domain.entity.Feature;

public abstract class ReportUtils implements Feature.ExecutionReport {
    public static Feature.ExecutionReport SUCCESS = new Feature.ExecutionReport() {
        @Override
        public boolean isSuccess() {
            return true;
        }
    };

    public static Feature.ExecutionReport FAILURE = new Feature.ExecutionReport() {
        @Override
        public boolean isSuccess() {
            return false;
        }
    };
}
