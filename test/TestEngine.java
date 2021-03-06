package test;

import util.Sleep;

/**
 * G�re le d�roulement des tests.
 * @author Stud
 *
 */


public class TestEngine {
	
	private Test test;
	
	public TestEngine(Test test)
	{
		this.test = test;
		test.init();
	}
	
	public void start()
	{
		long lastPauseTime = System.currentTimeMillis();
		for(int index = 0; index < test.getNbIteration(); ++index)
		{
			long currentTime = System.currentTimeMillis();
			if(!((currentTime - lastPauseTime) / 1000 > test.getConsecutiveLearnTime()))
			{
				System.out.println("Test "+(index+1)+" sur "+test.getNbIteration());
				test.onStart();
				if(test.isValidation())
					test.validTest();
				else
					test.proceedTest();
				test.onExit();
			}
			else
			{
				System.out.println("Début de pause. Durée: "+test.getPauseTime()+"s");
				test.onBreak();
				Sleep.sleep((long)(test.getPauseTime() * 1000));
				System.out.println("Fin de pause.");
				lastPauseTime = System.currentTimeMillis();
			}
		}
		test.terminate();
	}

}
