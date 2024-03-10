import javax.swing.*;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;

public class GamePanel  extends JPanel implements ActionListener{
    
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 3;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){

        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();


    }

    public void startGame(){
        running = true;
        x[0] = 100;
        y[0] = 100;
        spawnApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g){
        if (running){
            // for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){
            //     g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
            //     g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            // }
    
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
    
            for (int i = 0; i < bodyParts; i++){
                if (i == 0){
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.yellow);
            g.setFont(new Font("Arial", Font.BOLD, 15));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten)), g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }

    public void spawnApple(){

        boolean appleEaten = true;
        
        while (appleEaten){
            appleX = random.nextInt((int)((SCREEN_WIDTH - UNIT_SIZE)/UNIT_SIZE))*UNIT_SIZE;
            appleY = random.nextInt((int)((SCREEN_WIDTH - UNIT_SIZE)/UNIT_SIZE))*UNIT_SIZE;
            appleEaten = false;

            for (int i = 0; i < bodyParts; i++){
                if (x[i] == appleX && y[i] == appleY){
                    appleEaten = true;
                    break;
                }
            }
        }

    }

    public void move(){

        for (int i = bodyParts; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
                
        }

    }

    public void checkApple(){

        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            spawnApple();
        }

    }

    public void checkCollisions(){
        //Collision with body
        for(int i = bodyParts; i > 0; i--){
            if ((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }

        //Collision with L border
        if (x[0] < 0){
            x[0] = SCREEN_WIDTH;
            //running = false;
        }

        if (x[0] > SCREEN_WIDTH){
            x[0] = 0;
            //running = false;
        }

        if (y[0] < 0){
            y[0] = SCREEN_HEIGHT;
            //running = false;
        }

        if (y[0] > SCREEN_HEIGHT){
            y[0] = 0;
            //running = false;
        }

        if (!running){
            timer.stop();
        }

    }

    public void gameOver(Graphics g){
        //Game Over text
        // g.setColor(Color.red);
        // g.setFont(new Font("Ink Free", Font.BOLD, 75));
        // FontMetrics metrics1 = getFontMetrics(g.getFont());
        // g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

        JOptionPane.showMessageDialog(this, "Game Over! Your Score: " + applesEaten, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        restartGame();
        return;

    }

    public void restartGame(){
        applesEaten = 0;
        bodyParts = 3;
        direction = 'R';
        x[0] = 100;
        y[0] = 100;
        startGame();
    }

    @Override
    public void actionPerformed(ActionEvent e){

        if (running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();

    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }

        }
    }

}
