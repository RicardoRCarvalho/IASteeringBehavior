package cars.student;

import cars.engine.Car;
import cars.engine.Vector2;
import cars.engine.World;

import java.awt.*;

import static cars.engine.Vector2.vec2;

public class StudentCarSeek extends Car {
    public StudentCarSeek() {
        super(settings ->
          settings
            .color(Color.RED)
            .randomOrientation()
        );
    }

    boolean EnteredSeek = false;
    boolean distanceSetUp = false;
    double StartDistance;
    double distance;
    double Sx = getPosition().x;
    double Sy = getPosition().y;
    Vector2 Sf;


    /**
     * Deve calcular o steering behavior para esse carro
     * O parametro world contem diversos metodos utilitários:
     * world.getClickPos(): Retorna um vector2D com a posição do último click,
     * ou nulo se nenhum click foi dado ainda
     * - world.getMousePos(): Retorna um vector2D com a posição do cursor do mouse
     * - world.getNeighbors(): Retorna os carros vizinhos. Não inclui o próprio carro.
     * Opcionalmente, você pode passar o raio da vizinhança. Se o raio não for
     * fornecido retornará os demais carros.
     * - world.getSecs(): Indica quantos segundos transcorreram desde o último quadro
     * Você ainda poderá chamar os seguintes metodos do carro para obter informações:
     * - getDirection(): Retorna um vetor unitário com a direção do veículo
     * - getPosition(): Retorna um vetor com a posição do carro
     * - getMass(): Retorna a massa do carro
     * - getMaxSpeed(): Retorna a velocidade de deslocamento maxima do carro em píxeis / s
     * - getMaxForce(): Retorna a forca maxima que pode ser aplicada sobre o carro
     */

    public Vector2 seek(final World world, double weight){
        System.out.println("seek");
        Sx = getPosition().x;
        Sy = getPosition().y;
        if (EnteredSeek){
            StartDistance=distance;
            EnteredSeek=false;
        }

        Vector2 Sf = world.getClickPos();

        if (Sf != null) {
            double dx = Sf.x - Sx;
            double dy = Sf.y - Sy;
            distance = (double) Math.sqrt(dx * dx + dy * dy);
            if (!distanceSetUp){
                StartDistance=distance;
                distanceSetUp=true;
            }

            if (distance<= StartDistance/2){
                state =2;
            }
            if (distance < 10.0f) {

                distanceSetUp=false;
                return null;

            }
            Vector2 acel = new Vector2(Sx - Sf.x, Sy - Sf.y);
            acel.x *= -1;
            acel.y *=-1;
            Vector2 finalVector = Vector2.multiply(acel, weight);
            return finalVector;
        }
        else {
            return vec2();
        }
    }
    public Vector2 arrive(final World world, double weight) {
        System.out.println("arrive");
        Sx = getPosition().x;
        Sy = getPosition().y;
        Vector2 Sf = world.getClickPos();
        EnteredSeek=true;

        if (Sf != null) {
            //System.out.println(world.getClickPos());
            //System.out.println("SF"+Sf);
            double dx = Sf.x - Sx;
            double dy = Sf.y - Sy;
            distance = (double) Math.sqrt(dx * dx + dy * dy);

            Vector2 acel = new Vector2(Sx - Sf.x, Sy - Sf.y);

            if (StartDistance!=distance){
                state = 5;
            }

            if (world.getClickPos() != Sf){

                //System.out.println("Change");
            }
            if (distance<= StartDistance/10 ) {

                return null;

            }


            return new Vector2(getDirection().x*-getSpeed()*(distance/100),getDirection().y*-getSpeed()*(distance/100));


        } else {
            return new Vector2(0, 0);
        }
    }
    public Vector2 avoid(final World world, double weight, double mult){
        System.out.println("avoid");
        Sx = getPosition().x;
        Sy = getPosition().y;
        Vector2 obstaclePosition = new Vector2(100,100);
        Vector2 dodge;

        Vector2 Sf = world.getClickPos();

        if (Sf != null) {

            Vector2 acel = new Vector2(Sx - Sf.x, Sy - Sf.y);

            double dx = Sf.x - Sx;
            double dy = Sf.y - Sy;
            distance = (double) Math.sqrt(dx * dx + dy * dy);

            dodge = new Vector2(Sx - obstaclePosition.x, Sy - obstaclePosition.y);
            double obstacleDistancex = obstaclePosition.x - Sx;
            double obstacleDistancey = obstaclePosition.y - Sy;
            double obstacleDistance = (double) Math.sqrt(obstacleDistancex * obstacleDistancex + obstacleDistancey * obstacleDistancey);
            //System.out.println(obstacleDistance);
            if (distance <= 10) {
                return null;
            }
            if (obstacleDistance <100){
                dodge = Vector2.multiply(dodge,mult);
                dodge = Vector2.add(dodge,acel);
                Vector2 finalVector = Vector2.multiply(dodge, weight);
                return finalVector;
            }
            if (!distanceSetUp){
                StartDistance=distance;
                distanceSetUp=true;
            }

            if (distance<= StartDistance/1.5){
                state =2;
            }




            acel.x *= -1;
            acel.y *=-1;
            Vector2 finalVector = Vector2.multiply(acel, weight);
            return finalVector;
        }
        else {
            return vec2();
        }

    }

    public Vector2 SeekAvoidArrive(final World world)
    {
        double closestDistance;

        Sx = getPosition().x;
        Sy = getPosition().y;
        Sf = world.getClickPos();
        Vector2 obstaclePosition = new Vector2(100,100);
        Vector2 dodge;

        if (Sf != null) {
            double dx = Sf.x - Sx;
            double dy = Sf.y - Sy;
            distance = (double) Math.sqrt(dx * dx + dy * dy);
            dodge = new Vector2(Sx - obstaclePosition.x, Sy - obstaclePosition.y);
            double obstacleDistancex = obstaclePosition.x - Sx;
            double obstacleDistancey = obstaclePosition.y - Sy;
            double obstacleDistance = (double) Math.sqrt(obstacleDistancex * obstacleDistancex + obstacleDistancey * obstacleDistancey);
            if (!distanceSetUp){
                StartDistance=distance;
                distanceSetUp=true;
            }

            if (distance<= StartDistance/1.5){
                System.out.println("arrive");
                StartDistance = distance;
                return new Vector2(getDirection().x*-getSpeed()*(distance/100),getDirection().y*-getSpeed()*(distance/100));
            }
            if (distance < 10.0f) {

                distanceSetUp=false;
                return null;

            }
            Vector2 acel = new Vector2(Sx - Sf.x, Sy - Sf.y);
            acel.x *= -1;
            acel.y *=-1;
            if (obstacleDistance < 100){
                dodge = Vector2.multiply(dodge,500);
                dodge = Vector2.add(dodge,acel);
                Vector2 finalVector = Vector2.multiply(dodge, 1);
                return finalVector;
            }
            else {
                Vector2 finalVector = Vector2.multiply(acel, 1);
                return finalVector;
            }

        }
        else {
            return vec2();
        }

    }

    int state = 3;
    @Override
    public Vector2 calculateSteering(final World world) {

        switch(state){
            case 1:
                return seek(world, 1);
            case 2:
                return arrive(world, 1);
            case 3:
                return SeekAvoidArrive(world);
            case 5:
                return avoid(world,1,500);
        }
        return null;
    }
}
