package editor.window;

import java.util.ArrayList;

import editor.action_listener.ActionEvent;
import editor.action_listener.ActionListener;
import engine.window.components.ComboBox;
import engine.window.components.Window;

public class LayerMenu extends Window implements ActionListener {
	private ComboBox<Integer> layer_cb;
	private ArrayList<ActionListener> action_listeners;

	public LayerMenu() {
		action_listeners = new ArrayList<ActionListener>();
		setTitle("Layer Menu");

		layer_cb = new ComboBox<Integer>();
		layer_cb.setTheme("layer_cb");
		layer_cb.addActionListener(this);

		add(layer_cb);
	}

	public void populateLayers(Integer num_layers) {
		layer_cb.removeAllItems();
		for (int i = 0; i < num_layers; i++) {
			layer_cb.addItem(i);
		}
		layer_cb.setSelected(0);
	}

	public Integer getSelection() {
		if(layer_cb.getNumChildren() <= 0)
			return 0;
		else
			return layer_cb.getSelected();
	}
	
	public void setSelection(int selected) {
		if(	selected + 1 <= layer_cb.getModel().getNumEntries() &&
			selected - 1 >= -1 )
		{
			layer_cb.setSelected(selected);
		} else { 
			System.out.println(
				"Invalid layer number:" + selected + 
				" ;Currently on:" + layer_cb.getSelected() +
				" ;TotalChildren:" + layer_cb.getModel().getNumEntries()
			);
		}
	}

	public void addActionListener(ActionListener listener) {
		action_listeners.add(listener);
	}

	private void fireActionEvent() {
		for (ActionListener ae : action_listeners) {
			ae.actionPerformed(new ActionEvent(this));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		fireActionEvent();
	}
}
