package at.jku.sea.cloud.rest.server.listeners;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import at.jku.sea.cloud.Workspace;

public class BufferListenerManager {

	public static final int TIMEOUT_SCALE = 100;

	private final WorkspaceBufferListener listener;
	private final Workspace workspace;
	private final long timeout;

	private final ScheduledExecutorService executor;
	private ScheduledFuture<?> thread;

	private final Runnable task = new Runnable() {
		@Override
		public void run() {
			workspace.removeListener(listener);
		}
	};

	public BufferListenerManager(Workspace workspace, WorkspaceBufferListener listener, long interval) {
		this.workspace = workspace;
		this.listener = listener;
		timeout = interval * TIMEOUT_SCALE;

		executor = Executors.newSingleThreadScheduledExecutor();
		schedule();
	}

	public void resetTimer() {
		cancel();
		schedule();
	}

	public void cancel() {
		thread.cancel(true);
	}

	private void schedule() {
		thread = executor.schedule(task, timeout, TimeUnit.MILLISECONDS);
	}

	public WorkspaceBufferListener getListener() {
		return listener;
	}

}
