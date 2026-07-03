package models;

import insilico.core.model.InsilicoModel;

public class ModelValidator implements models.interfaces.iModelValidator {

    InsilicoModel model;

    public ModelValidator(InsilicoModel model) {
        this.model = model;
    }

    @Override
    public InsilicoModel model() {
        return model;
    }
}
