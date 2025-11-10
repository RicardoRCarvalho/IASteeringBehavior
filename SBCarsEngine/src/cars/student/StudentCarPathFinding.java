package cars.student;

import cars.engine.Car;
import cars.engine.Vector2;
import cars.engine.World;

import java.awt.*;
import java.util.Arrays;
import java.util.Vector;

import static cars.engine.Vector2.vec2;

public class StudentCarPathFinding extends Car {
    public StudentCarPathFinding() {
        super(settings ->
                settings
                        .color(Color.YELLOW)
                        .randomOrientation()
                        .position(-400, 0)
                        .maxSpeed(150)
                        .maxForce(500)

        );

    }

    java.util.List<Vector2> waypoints = Arrays.asList(new Vector2(0, -300), new Vector2(400, 0),new Vector2(0, 300), new Vector2(-300, 0));
    int currentWaypoint = 0;

    public Vector2 pathFollowing(double weight){
        if(waypoints.isEmpty()){
            return vec2();
        }

        Vector2 target = waypoints.get(currentWaypoint);
        double distance = Vector2.distance(getPosition(), target);

        Vector2 direction = Vector2.subtract(target, getPosition()).normalize();

        if (Vector2.distance(getPosition(), target) < 70) {
            currentWaypoint = (currentWaypoint + 1) % waypoints.size(); // loop
        }
        Vector2 finalVector = Vector2.multiply(direction, weight);

        return finalVector;
    }

    @Override
    public Vector2 calculateSteering(final World world) {
        System.out.println(currentWaypoint);
        return pathFollowing(100);
    }
}

