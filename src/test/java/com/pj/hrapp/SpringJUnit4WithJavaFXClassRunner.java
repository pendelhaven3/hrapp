package com.pj.hrapp;

import java.util.concurrent.CountDownLatch;

import javax.swing.SwingUtilities;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

public class SpringJUnit4WithJavaFXClassRunner extends SpringJUnit4ClassRunner {

	private Throwable runException;
	
	public SpringJUnit4WithJavaFXClassRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}

	@Override
	public void run(RunNotifier notifier) {
		setupJavaFX();
		runInJavaFXThread(notifier);
	}

	private void runInJavaFXThread(RunNotifier notifier) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	try {
					runSuper(notifier);
				} catch (Throwable e) {
					runException = e;
				}
                countDownLatch.countDown();
            }});
        
        try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
        
        if (runException != null) {
        	throw new RuntimeException(runException);
        }
	}

	protected void runSuper(RunNotifier notifier) {
		super.run(notifier);
	}

	private void setupJavaFX() {
        final CountDownLatch latch = new CountDownLatch(1);
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JFXPanel(); 
                latch.countDown();
            }
        });
        
        try {
			latch.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
}
