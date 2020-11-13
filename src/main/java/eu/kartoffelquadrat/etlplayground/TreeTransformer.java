/*******************************************************************************
 * Copyright (c) 2008 The University of York.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 *     Maximilian Schiedermeier - refactoring, comments, support for true standalone via self-contained jars
 ******************************************************************************/
package eu.kartoffelquadrat.etlplayground;

import java.net.URISyntaxException;

import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.IEolModule;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;
import org.eclipse.epsilon.eol.models.ModelRepository;
import org.eclipse.epsilon.etl.EtlModule;

/**
 * This example demonstrates using the Epsilon Transformation Language, the M2M language of Epsilon. It creates a deep
 * copy of a provided tree model and modifies the label attribute of every processed node.
 *
 * @author Maximilian Schiedermeier
 */
public class TreeTransformer {

    // Relative resource location of the ETL file
    private static final String TRANSFORMATION_RULES_LOCATION = "trafos/Tree2Tree.etl";

    // This internal reference represents the EOL object that implements the transformation rules specified in the
    // Tree2Tree etl file. It is instantiated by this classes constructor.
    private IEolModule module;

    /**
     * The Transformer constructor loads and parses the provided Tree2Tree etl file, specifying the transformation
     * rules. Fails if the required etl file is missing or syntactically wrong.
     */
    public TreeTransformer() {

        module = new EtlModule();

        // Load and parse the corresponding ETL file.
        try {
            module.parse(ResourceUtils.getResourceURI(TRANSFORMATION_RULES_LOCATION));
        } catch (Exception e) {
            throw new RuntimeException("Unable locate ETL module.");
        }

        // Abort in case there were parse errors.
        if (!module.getParseProblems().isEmpty()) {
            for (ParseProblem problem : module.getParseProblems()) {
                throw new RuntimeException("Unable to parse provided ETL file: " + problem.getReason());
            }
        }
    }

    /**
     * @param inputModelLocation
     * @param outputModelLocation
     */
    public void transform(String inputModelLocation, String outputModelLocation) {

        // add the input / output models to this transformation module. (The models internally indicate whether they
        // are source / target models.)
        IModel emfInputModel = convertToEmfModel(inputModelLocation, true);
        IModel emfOutputModel = convertToEmfModel(outputModelLocation, false);
        ModelRepository moduleModelRepository = module.getContext().getModelRepository();
        moduleModelRepository.addModel(emfInputModel);
        moduleModelRepository.addModel(emfOutputModel);

        // Actually run the model transformation.
        try {
            module.execute();
        } catch (EolRuntimeException e) {
            e.printStackTrace();
        }

        // Persist the transformation outcome on disk.
        module.getContext().getModelRepository().dispose();
    }


    /**
     * Converts  provided model (location of xmi resource) to an EMF model object that inherently links the parsed model
     * to its meta-model.
     *
     * @param modelLocation location of a model resource (xmi file)
     * @param inputModel    flag to indicate whether the provided model reference is an input- or output-model
     *                      location.
     * @return
     */
    private IModel convertToEmfModel(String modelLocation, boolean inputModel) {
        String identifier = (inputModel ? "Source" : "Target");

        try {
            return createEmfModel(identifier, modelLocation, "metamodels/Tree.ecore", inputModel,
                    !inputModel);
        } catch (EolModelLoadingException | URISyntaxException e) {
            throw new RuntimeException("Unable to create EMF model bundle object based on provided model location.");
        }
    }

    /**
     * Creates an EMF model (bundled object) which can be used by the ETL module performing the transformation. The
     * bundle houses the referenced model as an object object + metamodel object + identifier + persistence settings
     */
    private EmfModel createEmfModel(String name, String model,
                                    String metamodel, boolean readOnLoad, boolean storeOnDisposal)
            throws EolModelLoadingException, URISyntaxException {
        EmfModel emfModel = new EmfModel();
        StringProperties properties = new StringProperties();
        properties.put(EmfModel.PROPERTY_NAME, name);
        properties.put(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI,
                ResourceUtils.getResourceURI(metamodel).toString());
        properties.put(EmfModel.PROPERTY_MODEL_URI,
                model);
        properties.put(EmfModel.PROPERTY_READONLOAD, readOnLoad + "");
        properties.put(EmfModel.PROPERTY_STOREONDISPOSAL,
                storeOnDisposal + "");
        emfModel.load(properties, (IRelativePathResolver) null);
        return emfModel;
    }

}
