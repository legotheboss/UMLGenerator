package domain;

import data_source.MegaContainer;

public interface IController {
	public MegaContainer retrieve();
	public void execute();
}
