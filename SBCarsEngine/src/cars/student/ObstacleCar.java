package cars.student;

import cars.engine.Car;
import cars.engine.Vector2;
import cars.engine.World;

import java.awt.*;
import java.util.Random;

import static cars.engine.Vector2.byAngle;
import static cars.engine.Vector2.vec2;

public class ObstacleCar extends Car {
    public ObstacleCar() {
        super(settings ->
                settings
                        .color(Color.BLUE)
                        .orientation(0)
                        .position(100, 100)

        );

    }

    @Override
    public Vector2 calculateSteering(final World world) {
        return null;
    }
}

