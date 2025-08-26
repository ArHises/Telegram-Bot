package view;

import javax.swing.*;
import java.awt.*;

public final class ManualSurveyMetaDialog extends JDialog {
    private final JTextField creatorField = new JTextField(18);
    private final JTextField topicField   = new JTextField(22);
    private final JSpinner   delaySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10080, 1)); // minutes (0..7 days)

    private boolean ok = false;

    public static Result showFor(Component parent) {
        Window owner = parent instanceof Window ? (Window) parent : SwingUtilities.getWindowAncestor(parent);
        ManualSurveyMetaDialog d = new ManualSurveyMetaDialog(owner);
        d.setVisible(true);
        return d.ok ? new Result(
                d.creatorField.getText().trim(),
                d.topicField.getText().trim(),
                (Integer) d.delaySpinner.getValue()   // minutes
        ) : null;
    }

    private ManualSurveyMetaDialog(Window owner) {
        super(owner, "Survey Details", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,8,6,8);
        c.gridy = 0; c.gridx = 0; c.anchor = GridBagConstraints.LINE_END;
        form.add(new JLabel("Creator:"), c);
        c.gridx = 1; c.anchor = GridBagConstraints.LINE_START; form.add(creatorField, c);

        c.gridy++; c.gridx = 0; c.anchor = GridBagConstraints.LINE_END;
        form.add(new JLabel("Topic:"), c);
        c.gridx = 1; c.anchor = GridBagConstraints.LINE_START; form.add(topicField, c);

        c.gridy++; c.gridx = 0; c.anchor = GridBagConstraints.LINE_END;
        form.add(new JLabel("Delay (minutes):"), c);
        c.gridx = 1; c.anchor = GridBagConstraints.LINE_START;
        delaySpinner.setPreferredSize(new Dimension(90, delaySpinner.getPreferredSize().height));
        form.add(delaySpinner, c);

        JButton cancel = new JButton("Cancel");
        JButton okBtn  = new JButton("OK");
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(cancel); buttons.add(okBtn);

        cancel.addActionListener(e -> { ok = false; dispose(); });
        okBtn.addActionListener(e -> { ok = true; dispose(); });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(form, BorderLayout.CENTER);
        getContentPane().add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    public static final class Result {
        public final String creatorName;
        public final String topic;
        public final int delayMinutes;
        public Result(String creatorName, String topic, int delayMinutes) {
            this.creatorName = creatorName;
            this.topic = topic;
            this.delayMinutes = delayMinutes;
        }
    }
}
