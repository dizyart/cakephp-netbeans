package org.cakephp.netbeans.io;

import java.io.Reader;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputWriter;

/**
 * @author junichi11
 */
public class RedirectInputOutput implements InputOutput{
	private InputOutput io;
	private Reader reader;
	public RedirectInputOutput(Reader reader){
		io = IOProvider.getDefault().getIO("CakePHP Framework", false);
		this.reader = reader;
	}

	@Override
	public OutputWriter getOut() {
		return io.getOut();
	}

	@Override
	public Reader getIn() {
		return reader;
	}

	@Override
	public OutputWriter getErr() {
		return io.getErr();
	}

	@Override
	public void closeInputOutput() {
		io.closeInputOutput();
	}

	@Override
	public boolean isClosed() {
		return io.isClosed();
	}

	@Override
	public void setOutputVisible(boolean value) {
		io.setOutputVisible(value);
	}

	@Override
	public void setErrVisible(boolean value) {
		io.setErrVisible(value);
	}

	@Override
	public void setInputVisible(boolean value) {
		io.setInputVisible(value);
	}

	@Override
	public void select() {
		io.select();
	}

	@Override
	public boolean isErrSeparated() {
		return io.isErrSeparated();
	}

	@Override
	public void setErrSeparated(boolean value) {
		io.setErrSeparated(value);
	}

	@Override
	public boolean isFocusTaken() {
		return io.isFocusTaken();
	}

	@Override
	public void setFocusTaken(boolean value) {
		io.setFocusTaken(value);
	}

	@Override
	public Reader flushReader() {
		return io.flushReader();
	}
}
