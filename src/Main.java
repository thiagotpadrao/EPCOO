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

	Enemy(int [] states, double [] X, double [] Y, double [] V, double [] angle, double [] RV, double [] explosion_start, double [] explosion_end, double radius, long nextEnemy) {
		this.states = states;						// estados
		this.X = X;					// coordenadas x
		this.Y = Y;					// coordenadas y
		this.V = V;					// velocidades
		this.angle = angle;				// ângulos (indicam direção do movimento)
		this.RV = RV;					// velocidades de rotação
		this.explosion_start = explosion_start;		// instantes dos inícios das explosões
		this.explosion_end = explosion_end;		// instantes dos finais da explosões			// instantes do próximo tiro
		this.radius = radius;								// raio (tamanho do inimigo 1)
		this.nextEnemy = nextEnemy;	
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
}

class Enemy1 {
	private Enemy e;
	private long [] nextShoot;

	Enemy1(int [] states, double [] X, double [] Y, double [] V, double [] angle, double [] RV, double [] explosion_start, double [] explosion_end, double radius, long nextEnemy, long [] nextShoot) {
		this.e = new Enemy(states, X, Y, V, angle, RV, explosion_start, explosion_end, radius, nextEnemy);
		this.nextShoot = nextShoot;
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

class Projectile {
	int [] states;					// estados
	double [] X;				// coordenadas x
	double [] Y;				// coordenadas y
	double [] VX;				// velocidades no eixo x
	double [] VY;				// velocidades no eixo y

	public Projectile(int [] states, double [] X, double [] Y, double [] VX, double [] VY) {
		this.states = states; 					// estados
		this.X = X;				// coordenadas x
		this.Y = Y;				// coordenadas y
		this.VX = VX;				// velocidades no eixo x
		this.VY = VY;
	} 
}

class PlayerProjectile {
	private Projectile p;

	public PlayerProjectile(int [] states, double [] X, double [] Y, double [] VX, double [] VY) {
		this.p = new Projectile(states, X, Y, VX, VY);
		for(int i = 0; i < p.states.length; i++) states[i] = Main.INACTIVE;
	}

	int [] getStates() {
		return p.states;
	}
	double [] getX() {
		return p.X;
	}
	double [] getY() {
		return p.Y;
	}
	double [] getVX() {
		return p.VX;
	}
	double [] getVY() {
		return p.VY;
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
		
		PlayerProjectile player_projectile = new PlayerProjectile(new int [10], new double[10], new double[10], new double[10], new double[10]);

		/* variáveis dos inimigos tipo 1 */
		
		Enemy1 enemy1 = new Enemy1(new int[10], new double[10], new double[10], new double[10], new double[10], new double[10], new double[10], new double[10], 9.0, currentTime+200, new long[10]);
		
		/* variáveis dos inimigos tipo 2 */
		
		int [] enemy2_states = new int[10];						// estados
		double [] enemy2_X = new double[10];					// coordenadas x
		double [] enemy2_Y = new double[10];					// coordenadas y
		double [] enemy2_V = new double[10];					// velocidades
		double [] enemy2_angle = new double[10];				// ângulos (indicam direção do movimento)
		double [] enemy2_RV = new double[10];					// velocidades de rotação
		double [] enemy2_explosion_start = new double[10];		// instantes dos inícios das explosões
		double [] enemy2_explosion_end = new double[10];		// instantes dos finais das explosões
		double enemy2_spawnX = GameLib.WIDTH * 0.20;			// coordenada x do próximo inimigo tipo 2 a aparecer
		int enemy2_count = 0;									// contagem de inimigos tipo 2 (usada na "formação de voo")
		double enemy2_radius = 12.0;							// raio (tamanho aproximado do inimigo 2)
		long nextEnemy2 = currentTime + 7000;					// instante em que um novo inimigo 2 deve aparecer
		
		/* variáveis dos projéteis lançados pelos inimigos (tanto tipo 1, quanto tipo 2) */
		
		int [] e_projectile_states = new int[200];				// estados
		double [] e_projectile_X = new double[200];				// coordenadas x
		double [] e_projectile_Y = new double[200];				// coordenadas y
		double [] e_projectile_VX = new double[200];			// velocidade no eixo x
		double [] e_projectile_VY = new double[200];			// velocidade no eixo y
		double e_projectile_radius = 2.0;						// raio (tamanho dos projéteis inimigos)
		
		/* estrelas que formam o fundo de primeiro plano */
		
		double [] background1_X = new double[20];
		double [] background1_Y = new double[20];
		double background1_speed = 0.070;
		double background1_count = 0.0;
		
		/* estrelas que formam o fundo de segundo plano */
		
		double [] background2_X = new double[50];
		double [] background2_Y = new double[50];
		double background2_speed = 0.045;
		double background2_count = 0.0;
		
		/* inicializações */
		
		for(int i = 0; i < e_projectile_states.length; i++) e_projectile_states[i] = INACTIVE;
		for(int i = 0; i < enemy1.getStates().length; i++) enemy1.getStates()[i] = INACTIVE;
		for(int i = 0; i < enemy2_states.length; i++) enemy2_states[i] = INACTIVE;
		
		for(int i = 0; i < background1_X.length; i++){
			
			background1_X[i] = Math.random() * GameLib.WIDTH;
			background1_Y[i] = Math.random() * GameLib.HEIGHT;
		}
		
		for(int i = 0; i < background2_X.length; i++){
			
			background2_X[i] = Math.random() * GameLib.WIDTH;
			background2_Y[i] = Math.random() * GameLib.HEIGHT;
		}
						
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
				
				for(int i = 0; i < e_projectile_states.length; i++){
					
					double dx = e_projectile_X[i] - player.getX();
					double dy = e_projectile_Y[i] - player.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (player.getRadius() + e_projectile_radius) * 0.8){
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
				
				for(int i = 0; i < enemy2_states.length; i++){
					
					double dx = enemy2_X[i] - player.getX();
					double dy = enemy2_Y[i] - player.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (player.getRadius() + enemy2_radius) * 0.8){
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
				
				for(int i = 0; i < enemy2_states.length; i++){
					
					if(enemy2_states[i] == ACTIVE){
						
						double dx = enemy2_X[i] - player_projectile.getX()[k];
						double dy = enemy2_Y[i] - player_projectile.getY()[k];
						double dist = Math.sqrt(dx * dx + dy * dy);
						
						if(dist < enemy2_radius){
							
							enemy2_states[i] = EXPLODING;
							enemy2_explosion_start[i] = currentTime;
							enemy2_explosion_end[i] = currentTime + 500;
						}
					}
				}
			}
				
			/***************************/
			/* Atualizações de estados */
			/***************************/
			
			/* projeteis (player) */
			
			for(int i = 0; i < player_projectile.getStates().length; i++){
				
				if(player_projectile.getStates()[i] == ACTIVE){
					
					/* verificando se projétil saiu da tela */
					if(player_projectile.getY()[i] < 0) {
						
						player_projectile.getStates()[i] = INACTIVE;
					}
					else {
					
						player_projectile.getX()[i] += player_projectile.getVX()[i] * delta;
						player_projectile.getY()[i] += player_projectile.getVY()[i] * delta;
					}
				}
			}
			
			/* projeteis (inimigos) */
			
			for(int i = 0; i < e_projectile_states.length; i++){
				
				if(e_projectile_states[i] == ACTIVE){
					
					/* verificando se projétil saiu da tela */
					if(e_projectile_Y[i] > GameLib.HEIGHT) {
						
						e_projectile_states[i] = INACTIVE;
					}
					else {
					
						e_projectile_X[i] += e_projectile_VX[i] * delta;
						e_projectile_Y[i] += e_projectile_VY[i] * delta;
					}
				}
			}
			
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
																							
							int free = findFreeIndex(e_projectile_states);
							
							if(free < e_projectile_states.length){
								
								e_projectile_X[free] = enemy1.getX()[i];
								e_projectile_Y[free] = enemy1.getY()[i];
								e_projectile_VX[free] = Math.cos(enemy1.getAngle()[i]) * 0.45;
								e_projectile_VY[free] = Math.sin(enemy1.getAngle()[i]) * 0.45 * (-1.0);
								e_projectile_states[free] = 1;
								
								enemy1.getNextShoot()[i] = (long) (currentTime + 200 + Math.random() * 500);
							}
						}
					}
				}
			}
			
			/* inimigos tipo 2 */
			
			for(int i = 0; i < enemy2_states.length; i++){
				
				if(enemy2_states[i] == EXPLODING){
					
					if(currentTime > enemy2_explosion_end[i]){
						
						enemy2_states[i] = INACTIVE;
					}
				}
				
				if(enemy2_states[i] == ACTIVE){
					
					/* verificando se inimigo saiu da tela */
					if(	enemy2_X[i] < -10 || enemy2_X[i] > GameLib.WIDTH + 10 ) {
						
						enemy2_states[i] = INACTIVE;
					}
					else {
						
						boolean shootNow = false;
						double previousY = enemy2_Y[i];
												
						enemy2_X[i] += enemy2_V[i] * Math.cos(enemy2_angle[i]) * delta;
						enemy2_Y[i] += enemy2_V[i] * Math.sin(enemy2_angle[i]) * delta * (-1.0);
						enemy2_angle[i] += enemy2_RV[i] * delta;
						
						double threshold = GameLib.HEIGHT * 0.30;
						
						if(previousY < threshold && enemy2_Y[i] >= threshold) {
							
							if(enemy2_X[i] < GameLib.WIDTH / 2) enemy2_RV[i] = 0.003;
							else enemy2_RV[i] = -0.003;
						}
						
						if(enemy2_RV[i] > 0 && Math.abs(enemy2_angle[i] - 3 * Math.PI) < 0.05){
							
							enemy2_RV[i] = 0.0;
							enemy2_angle[i] = 3 * Math.PI;
							shootNow = true;
						}
						
						if(enemy2_RV[i] < 0 && Math.abs(enemy2_angle[i]) < 0.05){
							
							enemy2_RV[i] = 0.0;
							enemy2_angle[i] = 0.0;
							shootNow = true;
						}
																		
						if(shootNow){

							double [] angles = { Math.PI/2 + Math.PI/8, Math.PI/2, Math.PI/2 - Math.PI/8 };
							int [] freeArray = findFreeIndex(e_projectile_states, angles.length);

							for(int k = 0; k < freeArray.length; k++){
								
								int free = freeArray[k];
								
								if(free < e_projectile_states.length){
									
									double a = angles[k] + Math.random() * Math.PI/6 - Math.PI/12;
									double vx = Math.cos(a);
									double vy = Math.sin(a);
										
									e_projectile_X[free] = enemy2_X[i];
									e_projectile_Y[free] = enemy2_Y[i];
									e_projectile_VX[free] = vx * 0.30;
									e_projectile_VY[free] = vy * 0.30;
									e_projectile_states[free] = 1;
								}
							}
						}
					}
				}
			}
			
			/* verificando se novos inimigos (tipo 1) devem ser "lançados" */
			
			enemy1.throwNew(currentTime);
			
			/* verificando se novos inimigos (tipo 2) devem ser "lançados" */
			
			if(currentTime > nextEnemy2){
				
				int free = findFreeIndex(enemy2_states);
								
				if(free < enemy2_states.length){
					
					enemy2_X[free] = enemy2_spawnX;
					enemy2_Y[free] = -10.0;
					enemy2_V[free] = 0.42;
					enemy2_angle[free] = (3 * Math.PI) / 2;
					enemy2_RV[free] = 0.0;
					enemy2_states[free] = ACTIVE;

					enemy2_count++;
					
					if(enemy2_count < 10){
						
						nextEnemy2 = currentTime + 120;
					}
					else {
						
						enemy2_count = 0;
						enemy2_spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
						nextEnemy2 = (long) (currentTime + 3000 + Math.random() * 3000);
					}
				}
			}
			
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
			
			GameLib.setColor(Color.DARK_GRAY);
			background2_count += background2_speed * delta;
			
			for(int i = 0; i < background2_X.length; i++){
				
				GameLib.fillRect(background2_X[i], (background2_Y[i] + background2_count) % GameLib.HEIGHT, 2, 2);
			}
			
			/* desenhando plano de fundo próximo */
			
			GameLib.setColor(Color.GRAY);
			background1_count += background1_speed * delta;
			
			for(int i = 0; i < background1_X.length; i++){
				
				GameLib.fillRect(background1_X[i], (background1_Y[i] + background1_count) % GameLib.HEIGHT, 3, 3);
			}
						
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
		
			for(int i = 0; i < e_projectile_states.length; i++){
				
				if(e_projectile_states[i] == ACTIVE){
	
					GameLib.setColor(Color.RED);
					GameLib.drawCircle(e_projectile_X[i], e_projectile_Y[i], e_projectile_radius);
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
			
			for(int i = 0; i < enemy2_states.length; i++){
				
				if(enemy2_states[i] == EXPLODING){
					
					double alpha = (currentTime - enemy2_explosion_start[i]) / (enemy2_explosion_end[i] - enemy2_explosion_start[i]);
					GameLib.drawExplosion(enemy2_X[i], enemy2_Y[i], alpha);
				}
				
				if(enemy2_states[i] == ACTIVE){
			
					GameLib.setColor(Color.MAGENTA);
					GameLib.drawDiamond(enemy2_X[i], enemy2_Y[i], enemy2_radius);
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
