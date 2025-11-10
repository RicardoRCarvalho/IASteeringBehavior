package cars.student;

import cars.engine.Car;
import cars.engine.Vector2;
import cars.engine.World;

import java.awt.*;

import static cars.engine.Vector2.vec2;

public class StudentCarSeekAvoidArrive extends Car {
    public StudentCarSeekAvoidArrive() {
        super(settings ->
          settings
            .color(Color.RED)
            .randomOrientation()
            .maxSpeed(200)
        );
    }

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
        Sf = world.getClickPos();

        if (Sf != null) {
            Vector2 direction = Vector2.subtract(Sf, getPosition()).normalize();

            Vector2 finalVector = Vector2.multiply(direction, weight);

            return finalVector;
        }
        else {
            return vec2();
        }
    }
    public Vector2 arrive(final World world, double weight) {
        Sf = world.getClickPos();



        if(Sf != null) {
            Vector2 direction = Vector2.subtract(Sf, getPosition());
            double distance = Vector2.distance(getPosition(), Sf);
            //System.out.println(distance);

            double desiredSpeed = 0.0;
            if(distance < 0.1){
                Vector2 force = seek(world, 60);
                Vector2 finalVector = Vector2.negate(force);
                return finalVector;
            }

            if (distance < 70) {
                desiredSpeed = distance / 70;


                Vector2 desiredVelocity = direction.normalize().multiply(desiredSpeed);
                Vector2 force = desiredVelocity.subtract(getVelocity());

                Vector2 finalVector = Vector2.multiply(force, weight);
                return finalVector;
            }

        }

        return vec2(0.0, 0.0);

    }
    public Vector2 avoid(final World world, double weight){
        Vector2 Sf = new Vector2(100, 100);
        //System.out.println(Vector2.distance(Sf, getPosition()));

        if (Vector2.distance(Sf, getPosition()) < 100) {
            Vector2 direction = getPosition().subtract(Sf).normalize();
            double pushbackForce = weight * (1.0 - (Vector2.distance(Sf, getPosition()) / 100));
            Vector2 finalVector = Vector2.multiply(direction, pushbackForce);

            return finalVector;
        }
        else {
            return vec2();
        }
    }

    /*public Vector2 SeekAvoidArrive(final World world)
    {


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
            if (distance < 30.0f) {


                return null;

            }


            if (distance<= StartDistance/2){
                System.out.println("arrive");

                return new Vector2(getDirection().x*-getSpeed()*(distance/100),getDirection().y*-getSpeed()*(distance/100));
            }

            Vector2 acel = new Vector2(Sx - Sf.x, Sy - Sf.y);
            acel.x *= -1;
            acel.y *=-1;
            if (obstacleDistance < 100){
                dodge = Vector2.multiply(dodge,100);
                dodge = Vector2.add(dodge,acel);
                Vector2 finalVector = Vector2.multiply(dodge, 1);
                return finalVector;
            }
            else {
                double percentDeDistancia = (distance/StartDistance);
                Vector2 finalVector = Vector2.multiply(acel, 2);
                if (percentDeDistancia>1){
                    StartDistance = distance;

                }



                    if (percentDeDistancia<0.5){

                        Vector2 breaks = Vector2.multiply(getDirection(),-1);
                        breaks = Vector2.multiply(breaks,1-percentDeDistancia);
                        System.out.println(1-percentDeDistancia);
                        breaks = Vector2.multiply(breaks,2);
                        finalVector = Vector2.add(finalVector,breaks);


                        Vector2 breaks = Vector2.multiply(getDirection(),-1);
                        breaks = Vector2.multiply(breaks,getSpeed());
                        finalVector = Vector2.multiply(finalVector,1);
                        finalVector = Vector2.add(finalVector,breaks);




                        return finalVector;
                    }



                return finalVector;
            }

        }
        else {
            return vec2();
        }

    }*/

    @Override
    public Vector2 calculateSteering(final World world) {

        Vector2 seekVector = seek(world, 60);
        Vector2 avoidVector = avoid(world, 300);
        Vector2 arriveVector = arrive(world, 10);

        Vector2 movement = Vector2.add(seekVector, arriveVector);
        Vector2 finalVector = Vector2.add(movement, avoidVector);
       // System.out.println("Seek:  " + seekVector + "\nArrive: " + arriveVector + "\nAvoid: " + avoidVector + "\nTotal: " + finalVector);
       // System.out.println(finalVector.size());
        if(finalVector.isZero()) {
            return null;
        }
        return finalVector;
    }
}
