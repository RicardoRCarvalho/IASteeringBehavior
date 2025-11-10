package cars.student;

import cars.engine.Car;
import cars.engine.Vector2;
import cars.engine.World;

import java.awt.*;
import java.util.Random;

import static cars.engine.Vector2.byAngle;
import static cars.engine.Vector2.vec2;

public class StudentCarWanderFlee extends Car {
    public StudentCarWanderFlee() {
        super(settings ->
          settings
            .color(Color.GREEN)
            .randomOrientation()
            .maxSpeed(100)
        );
    }

    double wanderAngle = 0;
    private final Random random = new Random();

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

    public Vector2 RandomCoordinates() {

        int minX = -300;
        int maxX = 300;
        int minY = -300;
        int maxY = 300;

        Vector2 randomized = new Vector2(random.nextInt(maxX - minX + 1) + minX,random.nextInt(maxY - minY + 1) + minY);

        return randomized;
    }

    public Vector2 flee(final World world, double weight){
        Vector2 Sf = world.getMousePos();

        if (Sf != null && Vector2.distance(Sf, getPosition()) < 100) {
            Vector2 direction = getPosition().subtract(Sf).normalize();
            Vector2 finalVector = Vector2.multiply(direction, weight);

            return finalVector;
        }
        else {
            return vec2(0.0, 0.0);
        }
    }

    public Vector2 wander(final World world, double weight){
        double circleRadius = 60;
        double circleDistance = 30;
        double angleChange = 0.8;
        Vector2 circleCenter = getVelocity().normalize().multiply(circleDistance);
        Random random = new Random();
        wanderAngle += (random.nextDouble() - 0.5f) * angleChange;
        //System.out.println(wanderAngle);

        Vector2 displacement = byAngle(wanderAngle).multiply(circleRadius);

        Vector2 wanderTarget = getPosition().add(circleCenter).add(displacement);

        Vector2 steering = wanderTarget.subtract(getPosition()).normalize().multiply(weight);

        return steering;

    }

    public Vector2 wander2(final World world, double weight){
        double Sx = getPosition().x;
        double Sy = getPosition().y;
        //Vector2 Sf = RandomCoordinates();
        Vector2 fleeFrom = world.getMousePos();
        Vector2 circleCenter = getVelocity().normalize().multiply(16);
        Random random = new Random();
        wanderAngle += (random.nextFloat() - 0.5f) * 0.8;
        System.out.println(Vector2.distance(fleeFrom, getPosition()));

        Vector2 displacement = byAngle(wanderAngle).multiply(30);

        Vector2 Sf = getPosition().add(circleCenter).add(displacement);

        if (Sf != null && Vector2.distance(fleeFrom, getPosition()) < 100) {
            Vector2 acel = new Vector2(Sx - fleeFrom.x, Sy -fleeFrom.y);
            Vector2 finalVector = Vector2.multiply(acel, weight);

            return finalVector;
        }
        else {
            Vector2 acel = new Vector2(Sx - Sf.x, Sy - Sf.y);
            acel.x *= -1;
            acel.y *=-1;
            Vector2 finalVector = Vector2.multiply(acel, weight);
            return finalVector;
        }
    }

    @Override
    public Vector2 calculateSteering(final World world) {
        Vector2 wanderVector = wander(world, 50);
        Vector2 fleeVector = flee(world, 100);

        Vector2 movement = Vector2.add(wanderVector, fleeVector);
       // System.out.println("Seek:  " + wanderVector + "\nFlee: " + fleeVector + "\nTotal: " + movement);
        return movement;
    }
}
