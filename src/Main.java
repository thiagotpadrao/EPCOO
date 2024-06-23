import java.awt.Color;

class Player {
	private int state;								// estado
	private double X;					// coordenada x
	private double Y;				// coordenada y
	private double VX;								// velocidade no eixo x
	private double VY;								// velocidade no eixo y
	private double radius;							// raio (tamanho aproximado do player)
	private double explosion_start;						// instante do início da explosão
	private double explosion_end;						// instante do final da explosão
	private long nextShot;

	public Player(double GamelibWidth, double GamelibHeight, long currentTime) {
		this.state = Main.ACTIVE;
		this.X = GamelibWidth/ 2;					// coordenada x
		this.Y = GamelibHeight * 0.90;				// coordenada y
		this.VX = 0.25;								// velocidade no eixo x
		this.VY = 0.25;								// velocidade no eixo y
		this.radius = 12.0;							// raio (tamanho aproximado do player)
		this.explosion_start = 0;						// instante do início da explosão
		this.explosion_end = 0;						// instante do final da explosão
		this.nextShot = currentTime;
	}
	public double getVX() {
		return VX;
	}
	public double getVY() {
		return VY;
	}
	public double getX() {
		return X;
	}
	public double getY() {
		return Y;
	}
	public double getExplosion_end() {
		return explosion_end;
	}
	public double getExplosion_start() {
		return explosion_start;
	}
	public long getNextShot() {
		return nextShot;
	}
	public double getRadius() {
		return radius;
	}
	public int getState() {
		return state;
	}
	public void explode(long currentTime) {
		state = Main.EXPLODING;
		explosion_start = currentTime;
		explosion_end = currentTime + 2000;
	}
	public void reset(long currentTime) {
		if(state == Main.EXPLODING){

			if(currentTime > explosion_end){
				
				state = Main.ACTIVE;
			}
		}
	}

	public boolean keys(long delta, long currentTime, PlayerProjectile p) {
		if(this.state == Main.ACTIVE){
			if(GameLib.iskeyPressed(GameLib.KEY_UP)) Y -= delta * VY;
			if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) Y += delta * VY;
			if(GameLib.iskeyPressed(GameLib.KEY_LEFT)) X -= delta * VX;
			if(GameLib.iskeyPressed(GameLib.KEY_RIGHT)) X += delta * VY;
			if(GameLib.iskeyPressed(GameLib.KEY_CONTROL)) {
				
				if(currentTime > this.getNextShot()){
					
					int free = Main.findFreeIndex(p.getStates());
											
					if(free < p.getStates().length){
						
						p.getX()[free] = this.X;
						p.getY()[free] = this.Y - 2 * this.radius;
						p.getVX()[free] = 0.0;
						p.getVY()[free] = -1.0;
						p.getStates()[free] = 1;
						this.nextShot = currentTime + 100;
					}
				}	
			}
			if(GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) return false;
		}
		return true;
	}

	public void screenLimits() {
		if(X < 0.0) X = 0.0;
		if(X >= GameLib.WIDTH) X = GameLib.WIDTH - 1;
		if(Y < 25.0) Y = 25.0;
		if(Y >= GameLib.HEIGHT) Y = GameLib.HEIGHT - 1;
	}

	public void draw(long currentTime) {	
		if(state == Main.EXPLODING){
				
			double alpha = (currentTime - explosion_start) / (explosion_end - explosion_start);
			GameLib.drawExplosion(X, Y, alpha);
		}
		else{
			
			GameLib.setColor(Color.BLUE);
			GameLib.drawPlayer(X, Y, radius);
		}
	}
}

class Enemy {
	private int [] states;						// estados
	private double [] X;					// coordenadas x
	private double [] Y;					// coordenadas y
	private double [] V;					// velocidades
	private double [] angle;				// ângulos (indicam direção do movimento)
	private double [] RV;					// velocidades de rotação
	private double [] explosion_start;		// instantes dos inícios das explosões
	private double [] explosion_end;		// instantes dos finais da explosões			// instantes do próximo tiro
	private double radius;								// raio (tamanho do inimigo 1)
	private long nextEnemy;					// instante em que um novo inimigo 1 deve aparecer

	Enemy() {
		this.states = new int[10]; 
		this.X = new double[10];;						// estados				// coordenadas x
		this.Y = new double[10];					// coordenadas y
		this.V = new double[10];					// velocidades
		this.angle = new double[10];				// ângulos (indicam direção do movimento)
		this.RV = new double[10];					// velocidades de rotação
		this.explosion_start = new double[10];		// instantes dos inícios das explosões
		this.explosion_end = new double[10];		// instantes dos finais da explosões			// instantes do próximo tiro
		for(int i = 0; i < this.states.length; i++) this.states[i] = Main.INACTIVE;
	}

	public int[] getStates() {
		return states;
	}
	public double[] getX() {
		return X;
	}
	public double[] getY() {
		return Y;
	}
	public double[] getV() {
		return V;
	}
	public double[] getAngle() {
		return angle;
	}
	public double[] getRV() {
		return RV;
	}
	public double[] getExplosion_start() {
		return explosion_start;
	}
	public double[] getExplosion_end() {
		return explosion_end;
	}
	public double getRadius() {
		return radius;
	}
	public long getNextEnemy() {
		return nextEnemy;
	}
	public void setNextEnemy(long nextEnemy) {
		this.nextEnemy = nextEnemy;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
}

class Enemy1 {
	private Enemy e;
	private long [] nextShoot;

	Enemy1(long currentTime) {
		this.e = new Enemy();
		this.e.setRadius(9.0);
		this.e.setNextEnemy(currentTime + 2000);
		this.nextShoot = new long[10];
	}

	public int[] getStates() {
		return e.getStates();
	}
	public double[] getX() {
		return e.getX();
	}
	public double[] getY() {
		return e.getY();
	}
	public double[] getV() {
		return e.getV();
	}
	public double[] getAngle() {
		return e.getAngle();
	}
	public double[] getRV() {
		return e.getRV();
	}
	public double[] getExplosion_start() {
		return e.getExplosion_start();
	}
	public double[] getExplosion_end() {
		return e.getExplosion_end();
	}
	public double getRadius() {
		return e.getRadius();
	}
	public long getNextEnemy() {
		return e.getNextEnemy();
	}
	public long[] getNextShoot() {
		return nextShoot;
	}

	public void throwNew(long currentTime) {
		if(currentTime > e.getNextEnemy()){
				
			int free = Main.findFreeIndex(this.getStates());
							
			if(free < this.getStates().length){
				
				this.getX()[free] = Math.random() * (GameLib.WIDTH - 20.0) + 10.0;
				this.getY()[free] = -10.0;
				this.getV()[free] = 0.20 + Math.random() * 0.15;
				this.getAngle()[free] = 3 * Math.PI / 2;
				this.getRV()[free] = 0.0;
				this.getStates()[free] = Main.ACTIVE;
				this.getNextShoot()[free] = currentTime + 500;
				e.setNextEnemy(currentTime + 500);
			}
		}
	}
}

class Enemy2 {
	private Enemy e;
	private double spawnX;
	private static int count = 0;

	Enemy2(long currentTime) {
		this.e = new Enemy();
		this.e.setRadius(12.0);
		this.e.setNextEnemy(currentTime);
		this.spawnX = GameLib.WIDTH * 0.20;
		count++;
	}

	public int[] getStates() {
		return e.getStates();
	}
	public double[] getX() {
		return e.getX();
	}
	public double[] getY() {
		return e.getY();
	}
	public double[] getV() {
		return e.getV();
	}
	public double[] getAngle() {
		return e.getAngle();
	}
	public double[] getRV() {
		return e.getRV();
	}
	public double[] getExplosion_start() {
		return e.getExplosion_start();
	}
	public double[] getExplosion_end() {
		return e.getExplosion_end();
	}
	public double getRadius() {
		return e.getRadius();
	}
	public long getNextEnemy() {
		return e.getNextEnemy();
	}
	public static int getCount() {
		return count;
	}
	public double getSpawnX() {
		return spawnX;
	}

	public void throwNew(long currentTime) {
		if(currentTime > this.e.getNextEnemy()){
				
			int free = Main.findFreeIndex(this.e.getStates());
							
			if(free < this.e.getStates().length){
				
				this.e.getX()[free] = this.getSpawnX();
				this.e.getY()[free] = -10.0;
				this.e.getV()[free] = 0.42;
				this.e.getAngle()[free] = (3 * Math.PI) / 2;
				this.e.getRV()[free] = 0.0;
				this.e.getStates()[free] = Main.ACTIVE;

				count++;
				
				if(Enemy2.getCount() < 10){
					
					this.e.setNextEnemy(currentTime + 120);
				}
				else {
					count = 0;
					spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
					this.e.setNextEnemy((long) (currentTime + 3000 + Math.random() * 3000));
				}
			}
		}
		
	}
 }


class Projectile {
	private int [] states;					// estados
	private double [] X;				// coordenadas x
	private double [] Y;				// coordenadas y
	private double [] VX;				// velocidades no eixo x
	private double [] VY;				// velocidades no eixo y

	public Projectile(int [] states, double [] X, double [] Y, double [] VX, double [] VY) {
		this.states = states; 					// estados
		this.X = X;				// coordenadas x
		this.Y = Y;				// coordenadas y
		this.VX = VX;				// velocidades no eixo x
		this.VY = VY;
		for(int i = 0; i < this.states.length; i++) this.states[i] = Main.INACTIVE;
	}

	public int[] getStates() {
		return states;
	}
	public double[] getX() {
		return X;
	}
	public double[] getY() {
		return Y;
	}
	public double[] getVX() {
		return VX;
	}
	public double[] getVY() {
		return VY;
	}
	public void initializeStates() {
		for(int i = 0; i < this.getStates().length; i++) this.states[i] = Main.INACTIVE;
	}

	public void track(long delta) {
		for(int i = 0; i < this.getStates().length; i++){
				
			if(this.getStates()[i] == Main.ACTIVE){
				
				/* verificando se projétil saiu da tela */
				if(this.getY()[i] < 0 || this.getY()[i] > GameLib.HEIGHT ) {
					
					this.getStates()[i] = Main.INACTIVE;
				}
				else {
				
					this.getX()[i] += this.getVX()[i] * delta;
					this.getY()[i] += this.getVY()[i] * delta;
				}
			}
		}
	}
}

class PlayerProjectile {
	private Projectile p;

	public PlayerProjectile() {
		this.p = new Projectile(new int [10], new double[10], new double[10], new double[10], new double[10]);
		this.p.initializeStates();
	}

	public int [] getStates() {
		return p.getStates();
	}
	public double [] getX() {
		return p.getX();
	}
	public double [] getY() {
		return p.getY();
	}
	public double [] getVX() {
		return p.getVX();
	}
	public double [] getVY() {
		return p.getVY();
	}
	public void track(long delta) {
		p.track(delta);
	}
}
class EnemyProjectile {
	private Projectile p;
	private double radius;
	public EnemyProjectile() {
		this.p = new Projectile(new int [200], new double[200], new double[200], new double[200], new double[200]);
		this.p.initializeStates();
		this.radius = 2.0;
	}

	public int [] getStates() {
		return p.getStates();
	}
	public double [] getX() {
		return p.getX();
	}
	public double [] getY() {
		return p.getY();
	}
	public double [] getVX() {
		return p.getVX();
	}
	public double [] getVY() {
		return p.getVY();
	}
	public double getRadius() {
		return radius;
	}
	public void track(long delta) {
		p.track(delta);
	}
}




class Background {
			
	private double [] X;
	private double [] Y;
	private double speed;
	private double count;

	public Background(int X, int Y, double speed, double count){
		this.X = new double [X];
		this.Y = new double [Y];
		this.speed = speed;
		this.count = count;
		for(int i = 0; i < this.X.length; i++){
			
			this.X[i] = Math.random() * GameLib.WIDTH;
			this.Y[i] = Math.random() * GameLib.HEIGHT;
		}
	}
	
	public void draw(Long delta, Color cor, int size){    //metodo pra desenhar o background
		GameLib.setColor(cor);
			count += speed * delta;
			
			for(int i = 0; i < X.length; i++){
				
				GameLib.fillRect(X[i], (Y[i] + count) % GameLib.HEIGHT, size, size);
			}
	}

	/*Getters e Setters*/
	public double[] get_X() {
		return X;
	}
	public double[] get_Y() {
		return Y;
	}
	public double get_count() {
		return count;
	}
	public double get_speed() {
		return speed;
	}
	public void set_X(double[] X) {
		this.X = X;
	}
	public void set_Y(double[] Y) {
		this.Y = Y;
	}
	public void set_count(double count) {
		this.count = count;
	}
	public void set_speed(double speed) {
		this.speed = speed;
	}
} 

class Estrela extends Background {
	public Estrela(int X, int Y, double speed, double count) {
		super(X, Y, speed, count);
	}
}

public class Main {
	
	/* Constantes relacionadas aos estados que os elementos   */
	/* do jogo (player, projeteis ou inimigos) podem assumir. */
	
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;
	

	/* Espera, sem fazer nada, até que o instante de tempo atual seja */
	/* maior ou igual ao instante especificado no parâmetro "time.    */
	
	public static void busyWait(long time){
		
		while(System.currentTimeMillis() < time) Thread.yield();
	}
	
	/* Encontra e devolve o primeiro índice do  */
	/* array referente a uma posição "inativa". */
	
	public static int findFreeIndex(int [] stateArray){
		
		int i;
		
		for(i = 0; i < stateArray.length; i++){
			
			if(stateArray[i] == INACTIVE) break;
		}
		
		return i;
	}
	
	/* Encontra e devolve o conjunto de índices (a quantidade */
	/* de índices é defnida através do parâmetro "amount") do */
	/* array, referentes a posições "inativas".               */ 

	public static int [] findFreeIndex(int [] stateArray, int amount){

		int i, k;
		int [] freeArray = { stateArray.length, stateArray.length, stateArray.length };
		
		for(i = 0, k = 0; i < stateArray.length && k < amount; i++){
				
			if(stateArray[i] == INACTIVE) { 
				
				freeArray[k] = i; 
				k++;
			}
		}
		
		return freeArray;
	}
	
	/* Método principal */
	
	public static void main(String [] args){

		/* Indica que o jogo está em execução */
		boolean running = true;

		/* variáveis usadas no controle de tempo efetuado no main loop */
		
		long delta;
		long currentTime = System.currentTimeMillis();

		/* inicializando player*/
		
		Player player = new Player(GameLib.WIDTH, GameLib.HEIGHT, currentTime);

		/* variáveis dos projéteis disparados pelo player */
		
		PlayerProjectile player_projectile = new PlayerProjectile();

		/* variáveis dos inimigos tipo 1 */
		
		Enemy1 enemy1 = new Enemy1(currentTime);
		
		/* variáveis dos inimigos tipo 2 */
		Enemy2 enemy2 = new Enemy2(currentTime);
		
		/* variáveis dos projéteis lançados pelos inimigos (tanto tipo 1, quanto tipo 2) */

		EnemyProjectile enemy_projectile = new EnemyProjectile();
		
		Estrela estrela1 = new Estrela(20, 20, 0.070, 0.0);
		Estrela estrela2 = new Estrela(50, 50, 0.045, 0.0);
		
		/* iniciado interface gráfica */
		
		GameLib.initGraphics();
		
		/*************************************************************************************************/
		/*                                                                                               */
		/* Main loop do jogo                                                                             */
		/*                                                                                               */
		/* O main loop do jogo possui executa as seguintes operações:                                    */
		/*                                                                                               */
		/* 1) Verifica se há colisões e atualiza estados dos elementos conforme a necessidade.           */
		/*                                                                                               */
		/* 2) Atualiza estados dos elementos baseados no tempo que correu desde a última atualização     */
		/*    e no timestamp atual: posição e orientação, execução de disparos de projéteis, etc.        */
		/*                                                                                               */
		/* 3) Processa entrada do usuário (teclado) e atualiza estados do player conforme a necessidade. */
		/*                                                                                               */
		/* 4) Desenha a cena, a partir dos estados dos elementos.                                        */
		/*                                                                                               */
		/* 5) Espera um período de tempo (de modo que delta seja aproximadamente sempre constante).      */
		/*                                                                                               */
		/*************************************************************************************************/
		
		while(running){
		
			/* Usada para atualizar o estado dos elementos do jogo    */
			/* (player, projéteis e inimigos) "delta" indica quantos  */
			/* ms se passaram desde a última atualização.             */
			
			delta = System.currentTimeMillis() - currentTime;
			
			/* Já a variável "currentTime" nos dá o timestamp atual.  */
			
			currentTime = System.currentTimeMillis();
			
			/***************************/
			/* Verificação de colisões */
			/***************************/
						
			if(player.getState() == ACTIVE){
				
				/* colisões player - projeteis (inimigo) */
				
				for(int i = 0; i < enemy_projectile.getStates().length; i++){
					
					double dx = enemy_projectile.getX()[i] - player.getX();
					double dy = enemy_projectile.getY()[i] - player.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (player.getRadius() + enemy_projectile.getRadius()) * 0.8){
						player.explode(currentTime);
					}
				}
			
				/* colisões player - inimigos */
							
				for(int i = 0; i < enemy1.getStates().length; i++){
					
					double dx = enemy1.getX()[i] - player.getX();
					double dy = enemy1.getY()[i] - player.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (player.getRadius() + enemy1.getRadius()) * 0.8){
						player.explode(currentTime);
					}
				}
				
				for(int i = 0; i < enemy2.getStates().length; i++){
					
					double dx = enemy2.getX()[i] - player.getX();
					double dy = enemy2.getY()[i] - player.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (player.getRadius() + enemy2.getRadius()) * 0.8){
						player.explode(currentTime);
					}
				}
			}
			
			/* colisões projeteis (player) - inimigos */
			
			for(int k = 0; k < player_projectile.getStates().length; k++){
				
				for(int i = 0; i < enemy1.getStates().length; i++){
										
					if(enemy1.getStates()[i] == ACTIVE){
					
						double dx = enemy1.getX()[i] - player_projectile.getX()[k];
						double dy = enemy1.getY()[i] - player_projectile.getY()[k];
						double dist = Math.sqrt(dx * dx + dy * dy);
						
						if(dist < enemy1.getRadius()){
							
							enemy1.getStates()[i] = EXPLODING;
							enemy1.getExplosion_start()[i] = currentTime;
							enemy1.getExplosion_end()[i] = currentTime + 500;
						}
					}
				}
				
				for(int i = 0; i < enemy2.getStates().length; i++){
					
					if(enemy2.getStates()[i] == ACTIVE){
						
						double dx = enemy2.getX()[i] - player_projectile.getX()[k];
						double dy = enemy2.getY()[i] - player_projectile.getY()[k];
						double dist = Math.sqrt(dx * dx + dy * dy);
						
						if(dist < enemy2.getRadius()){
							
							enemy2.getStates()[i] = EXPLODING;
							enemy2.getExplosion_start()[i] = currentTime;
							enemy2.getExplosion_end()[i] = currentTime + 500;
						}
					}
				}
			}
				
			/***************************/
			/* Atualizações de estados */
			/***************************/
			
			/* projeteis (player) */
			
			player_projectile.track(delta);
			
			/* projeteis (inimigos) */
			
			enemy_projectile.track(delta);

			/* inimigos tipo 1 */
			
			for(int i = 0; i < enemy1.getStates().length; i++){
				
				if(enemy1.getStates()[i] == EXPLODING){
					
					if(currentTime > enemy1.getExplosion_end()[i]){
						
						enemy1.getStates()[i] = INACTIVE;
					}
				}
				
				if(enemy1.getStates()[i] == ACTIVE){
					
					/* verificando se inimigo saiu da tela */
					if(enemy1.getY()[i] > GameLib.HEIGHT + 10) {
						
						enemy1.getStates()[i] = INACTIVE;
					}
					else {
					
						enemy1.getX()[i] += enemy1.getV()[i] * Math.cos(enemy1.getAngle()[i]) * delta;
						enemy1.getY()[i] += enemy1.getV()[i] * Math.sin(enemy1.getAngle()[i]) * delta * (-1.0);
						enemy1.getAngle()[i] += enemy1.getRV()[i] * delta;
						
						if(currentTime > enemy1.getNextShoot()[i] && enemy1.getY()[i] < player.getY()){
																							
							int free = findFreeIndex(enemy_projectile.getStates());
							
							if(free < enemy_projectile.getStates().length){
								
								enemy_projectile.getX()[free] = enemy1.getX()[i];
								enemy_projectile.getY()[free] = enemy1.getY()[i];
								enemy_projectile.getVX()[free] = Math.cos(enemy1.getAngle()[i]) * 0.45;
								enemy_projectile.getVY()[free] = Math.sin(enemy1.getAngle()[i]) * 0.45 * (-1.0);
								enemy_projectile.getStates()[free] = 1;
								
								enemy1.getNextShoot()[i] = (long) (currentTime + 200 + Math.random() * 500);
							}
						}
					}
				}
			}
			
			/* inimigos tipo 2 */
			
			for(int i = 0; i < enemy2.getStates().length; i++){
				
				if(enemy2.getStates()[i] == EXPLODING){
					
					if(currentTime > enemy2.getExplosion_end()[i]){
						
						enemy2.getStates()[i] = INACTIVE;
					}
				}
				
				if(enemy2.getStates()[i] == ACTIVE){
					
					/* verificando se inimigo saiu da tela */
					if(	enemy2.getX()[i] < -10 || enemy2.getX()[i] > GameLib.WIDTH + 10 ) {
						
						enemy2.getStates()[i] = INACTIVE;
					}
					else {
						
						boolean shootNow = false;
						double previousY = enemy2.getY()[i];
												
						enemy2.getX()[i] += enemy2.getV()[i] * Math.cos(enemy2.getAngle()[i]) * delta;
						enemy2.getY()[i] += enemy2.getV()[i] * Math.sin(enemy2.getAngle()[i]) * delta * (-1.0);
						enemy2.getAngle()[i] += enemy2.getRV()[i] * delta;
						
						double threshold = GameLib.HEIGHT * 0.30;
						
						if(previousY < threshold && enemy2.getY()[i] >= threshold) {
							
							if(enemy2.getX()[i] < GameLib.WIDTH / 2) enemy2.getRV()[i] = 0.003;
							else enemy2.getRV()[i] = -0.003;
						}
						
						if(enemy2.getRV()[i] > 0 && Math.abs(enemy2.getAngle()[i] - 3 * Math.PI) < 0.05){
							
							enemy2.getRV()[i] = 0.0;
							enemy2.getAngle()[i] = 3 * Math.PI;
							shootNow = true;
						}
						
						if(enemy2.getRV()[i] < 0 && Math.abs(enemy2.getAngle()[i]) < 0.05){
							
							enemy2.getRV()[i] = 0.0;
							enemy2.getAngle()[i] = 0.0;
							shootNow = true;
						}
																		
						if(shootNow){

							double [] angles = { Math.PI/2 + Math.PI/8, Math.PI/2, Math.PI/2 - Math.PI/8 };
							int [] freeArray = findFreeIndex(enemy_projectile.getStates(), angles.length);

							for(int k = 0; k < freeArray.length; k++){
								
								int free = freeArray[k];
								
								if(free < enemy_projectile.getStates().length){
									
									double a = angles[k] + Math.random() * Math.PI/6 - Math.PI/12;
									double vx = Math.cos(a);
									double vy = Math.sin(a);
										
									enemy_projectile.getX()[free] = enemy2.getX()[i];
									enemy_projectile.getY()[free] = enemy2.getY()[i];
									enemy_projectile.getVX()[free] = vx * 0.30;
									enemy_projectile.getVY()[free] = vy * 0.30;
									enemy_projectile.getStates()[free] = 1;
								}
							}
						}
					}
				}
			}
			
			/* verificando se novos inimigos (tipo 1) devem ser "lançados" */
			
			enemy1.throwNew(currentTime);
			
			/* verificando se novos inimigos (tipo 2) devem ser "lançados" */
			
			enemy2.throwNew(currentTime);
			
			/* Verificando se a explosão do player já acabou.         */
			/* Ao final da explosão, o player volta a ser controlável */
			player.reset(currentTime);
			
			/********************************************/
			/* Verificando entrada do usuário (teclado) */
			/********************************************/
			// vamo fazer um método do player pra isso dps, mas preisaria criar o objeto do projétil antes
			
			running = player.keys(delta, currentTime, player_projectile);
			
			/* Verificando se coordenadas do player ainda estão dentro	*/
			/* da tela de jogo após processar entrada do usuário.       */
			
			player.screenLimits();

			/*******************/
			/* Desenho da cena */
			/*******************/
			
			/* desenhando plano fundo distante */
			
			estrela1.draw(delta, Color.GRAY, 3);
			estrela2.draw(delta, Color.DARK_GRAY, 2);
						
			/* desenhando player */
			
			player.draw(currentTime);
				
			
			/* deenhando projeteis (player) */
			
			for(int i = 0; i < player_projectile.getStates().length; i++){
				
				if(player_projectile.getStates()[i] == ACTIVE){
					
					GameLib.setColor(Color.GREEN);
					GameLib.drawLine(player_projectile.getX()[i], player_projectile.getY()[i] - 5, player_projectile.getX()[i], player_projectile.getY()[i] + 5);
					GameLib.drawLine(player_projectile.getX()[i] - 1, player_projectile.getY()[i] - 3, player_projectile.getX()[i] - 1, player_projectile.getY()[i] + 3);
					GameLib.drawLine(player_projectile.getX()[i] + 1, player_projectile.getY()[i] - 3, player_projectile.getX()[i] + 1, player_projectile.getY()[i] + 3);
				}
			}
			
			/* desenhando projeteis (inimigos) */
		
			for(int i = 0; i < enemy_projectile.getStates().length; i++){
				
				if(enemy_projectile.getStates()[i] == ACTIVE){
	
					GameLib.setColor(Color.RED);
					GameLib.drawCircle(enemy_projectile.getX()[i], enemy_projectile.getY()[i], enemy_projectile.getRadius());
				}
			}
			
			/* desenhando inimigos (tipo 1) */
			
			for(int i = 0; i < enemy1.getStates().length; i++){
				
				if(enemy1.getStates()[i] == EXPLODING){
					
					double alpha = (currentTime - enemy1.getExplosion_start()[i]) / (enemy1.getExplosion_end()[i] - enemy1.getExplosion_start()[i]);
					GameLib.drawExplosion(enemy1.getX()[i], enemy1.getY()[i], alpha);
				}
				
				if(enemy1.getStates()[i] == ACTIVE){
			
					GameLib.setColor(Color.CYAN);
					GameLib.drawCircle(enemy1.getX()[i], enemy1.getY()[i], enemy1.getRadius());
				}
			}
			
			/* desenhando inimigos (tipo 2) */
			
			for(int i = 0; i < enemy2.getStates().length; i++){
				
				if(enemy2.getStates()[i] == EXPLODING){
					
					double alpha = (currentTime - enemy2.getExplosion_start()[i]) / (enemy2.getExplosion_end()[i] - enemy2.getExplosion_start()[i]);
					GameLib.drawExplosion(enemy2.getX()[i], enemy2.getY()[i], alpha);
				}
				
				if(enemy2.getStates()[i] == ACTIVE){
			
					GameLib.setColor(Color.MAGENTA);
					GameLib.drawDiamond(enemy2.getX()[i], enemy2.getY()[i], enemy2.getRadius());
				}
			}
			
			/* chamama a display() da classe GameLib atualiza o desenho exibido pela interface do jogo. */
			
			GameLib.display();
			
			/* faz uma pausa de modo que cada execução do laço do main loop demore aproximadamente 5 ms. */
			
			busyWait(currentTime + 5);
		}
		
		System.exit(0);
	}
}
