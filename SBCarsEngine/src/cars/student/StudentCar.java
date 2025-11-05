package cars.student;

import cars.engine.Car;
import cars.engine.Vector2;
import cars.engine.World;
import java.util.Random;

import java.awt.*;

import static cars.engine.Vector2.byAngle;
import static cars.engine.Vector2.vec2;

public class StudentCar extends Car {
    public StudentCar() {
        super(settings ->
                settings
                        .color(Color.BLUE)
                        .randomOrientation()

        );

    }
    boolean EnteredSeek = false;
    boolean distanceSetUp = false;
    double StartDistance;
    double distance;
    double Sx = getPosition().x;
    double Sy = getPosition().y;
    double wanderAngle = 0;

    public Vector2 RandomCoordinates() {


        Random random = new Random();

        int minX = -300;
        int maxX = 300;
        int minY = -300;
        int maxY = 300;

        Vector2 randomized = new Vector2(random.nextInt(maxX - minX + 1) + minX,random.nextInt(maxY - minY + 1) + minY);

        return randomized;

    }




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

        Sx = getPosition().x;
        Sy = getPosition().y;
        Vector2 Sf = world.getClickPos();
        EnteredSeek=true;

        if (Sf != null) {
            System.out.println(world.getClickPos());
            System.out.println("SF"+Sf);
            double dx = Sf.x - Sx;
            double dy = Sf.y - Sy;
            distance = (double) Math.sqrt(dx * dx + dy * dy);

            Vector2 acel = new Vector2(Sx - Sf.x, Sy - Sf.y);

            if (StartDistance!=distance){
                state = 5;
            }

            if (world.getClickPos() != Sf){

                System.out.println("Change");
            }
            if (distance<= StartDistance/10 ) {

                state = 5;

                return null;

            }


            return new Vector2(getDirection().x*-getSpeed()*(distance/100),getDirection().y*-getSpeed()*(distance/100));


        } else {
            return new Vector2(0, 0);
        }
    }
    public Vector2 flee(final World world, double weight){
        Sx = getPosition().x;
        Sy = getPosition().y;
        Vector2 Sf = world.getMousePos();

        if (Sf != null && Vector2.distance(Sf, getPosition()) > 100) {
            Vector2 acel = new Vector2(Sx - Sf.x, Sy -Sf.y);
            Vector2 finalVector = Vector2.multiply(acel, weight);

            return finalVector;
        }
        else {
            return vec2(0.0, 0.0);
        }
    }
    public Vector2 wander(final World world, double weight){
        Sx = getPosition().x;
        Sy = getPosition().y;
        Vector2 Sf = RandomCoordinates();

        if (Sf != null) {
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

    public Vector2 wander2(final World world, double weight){
        Sx = getPosition().x;
        Sy = getPosition().y;
        Vector2 Sf = RandomCoordinates();
        Vector2 circleCenter = getVelocity().normalize().multiply(16);
        Random random = new Random();
        wanderAngle += (random.nextFloat() - 0.5f) * 0.8;
        System.out.println(wanderAngle);

        Vector2 displacement = byAngle(wanderAngle).multiply(30);

        Sf = getPosition().add(circleCenter).add(displacement);

        if (Sf != null) {
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

    public Vector2 avoid(final World world, double weight, double mult){

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
            System.out.println(obstacleDistance);
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

    int state = 2;
    @Override
    public Vector2 calculateSteering(final World world) {

        switch(state){
            case 1:
                return seek(world, 1);

            case 2:
                return arrive(world, 1);

            case 3:
                return wander2(world,1);
            case 4:
                return flee(world,1);
            case 5:
                return avoid(world,1,500);
        }

        return null;
    }
}

