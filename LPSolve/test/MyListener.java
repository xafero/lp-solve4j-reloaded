import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import lpsolve.AbortListener;
import lpsolve.LogListener;
import lpsolve.LpSolve;
import lpsolve.LpSolveException;
import lpsolve.MsgListener;

public class MyListener implements AbortListener, LogListener, MsgListener {
	public int numCalls = 0;

	private LpSolve problem;

	public MyListener(LpSolve problem) {
		this.problem = problem;
	}

	public boolean abortfunc(LpSolve prob, Object handle) {
		numCalls++;
		assertEquals(problem, prob);
		assertEquals(new Integer(123), handle);
		return false;
	}

	public void logfunc(LpSolve prob, Object handle, String buf) {
		numCalls++;
		assertEquals(problem, prob);
		assertEquals(new Integer(123), handle);
		assertNotNull(buf);
	}

	public void msgfunc(LpSolve prob, Object handle, int code)
			throws LpSolveException {
		numCalls++;
		assertEquals(problem, prob);
		assertEquals(new Integer(123), handle);
		problem.getWorkingObjective();
	}
}