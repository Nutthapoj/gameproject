package game.obj;

public class Timer {
    private int timeRemaining; 
    private boolean running;
    private int initialTime; 

    public Timer() {
        this(60); 
    }
    
    public Timer(int initialTime) {
        this.initialTime = initialTime;
        timeRemaining = initialTime; 
        running = true;
    }

    public void startTimer() {
        running = true; 
        Thread timerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (timeRemaining > 0 && running) {
                    try {
                        Thread.sleep(1000); 
                        timeRemaining--;    
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        timerThread.start();
    }
    
    public int getTimeRemaining() {
        return timeRemaining;
    }
    
    public void stopTimer() {
        running = false;
    }
    
   
    public void resetTimer() {
        timeRemaining = initialTime;  
        running = false;              
        startTimer();                 
    }
}
