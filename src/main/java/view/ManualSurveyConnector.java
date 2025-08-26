package view;

import java.awt.Component;
import java.awt.Container;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.swing.JOptionPane;

public final class ManualSurveyConnector {

    private ManualSurveyConnector() {}

    /** Attach using the SurveyFrame (no edits to SurveyFrame). */
    public static void attach(SurveyFrame frame,
                              Function<String, model.User> userFactory,
                              Consumer<model.Survey> onBuilt) {
        DynamicSurvey ui = findDynamicSurvey(frame);
        if (ui == null) {
            System.err.println("[ManualSurveyConnector] DynamicSurvey NOT found in frame. "
                    + "Did you add it as a card?");
            JOptionPane.showMessageDialog(frame,
                    "DynamicSurvey was not found in the frame.\n"
                            + "Make sure the manual survey card is added before attaching.",
                    "ManualSurveyConnector", JOptionPane.WARNING_MESSAGE);
            return;
        }
        attachTo(ui, frame, userFactory, onBuilt);
    }

    /** Attach directly if you have the DynamicSurvey instance. */
    public static void attachTo(DynamicSurvey ui, java.awt.Component parentForDialogs,
                                Function<String, model.User> userFactory,
                                Consumer<model.Survey> onBuilt) {
        System.out.println("[ManualSurveyConnector] Attached to DynamicSurvey.");
        // If addRunListener doesn't exist or never fires, you won't see the next log.
        ui.addRunListener(e -> {
            System.out.println("[ManualSurveyConnector] Run clicked.");

            ManualSurveyMetaDialog.Result meta = ManualSurveyMetaDialog.showFor(parentForDialogs);
            if (meta == null) {
                System.out.println("[ManualSurveyConnector] User cancelled survey details dialog.");
                return;
            }

            model.User creator = userFactory.apply(meta.creatorName);
            if (creator == null) {
                System.err.println("[ManualSurveyConnector] userFactory returned null User.");
                return;
            }

            model.Survey survey = new model.Survey(creator, meta.topic, meta.delayMinutes);

            java.util.ArrayList<String> qs  = ui.getQuestions();
            java.util.ArrayList<java.util.ArrayList<String>> all = ui.getAllAnswers();

            for (int i = 0; i < qs.size(); i++) {
                String qText = qs.get(i) == null ? "" : qs.get(i).trim();
                if (qText.isEmpty()) continue;

                // Question(String text, List<String> options)
                java.util.ArrayList<String> options = new java.util.ArrayList<>();
                java.util.ArrayList<String> opts = (i < all.size()) ? all.get(i) : new java.util.ArrayList<>();
                for (String opt : opts) {
                    if (opt == null) continue;
                    String clean = opt.trim();
                    if (!clean.isEmpty()) options.add(clean);
                }
                model.Question q = new model.Question(qText, options);
                survey.addQuestion(q);
            }

            System.out.println("[ManualSurveyConnector] Survey built with "
                    + survey.getQuestions().size() + " questions.");
            onBuilt.accept(survey);
        });
    }

    /** Recursively find the DynamicSurvey panel inside a container. */
    private static DynamicSurvey findDynamicSurvey(Container root) {
        if (root == null) return null;
        for (Component c : root.getComponents()) {
            if (c instanceof DynamicSurvey) return (DynamicSurvey) c;
            if (c instanceof Container) {
                DynamicSurvey found = findDynamicSurvey((Container) c);
                if (found != null) return found;
            }
        }
        return null;
    }
}

