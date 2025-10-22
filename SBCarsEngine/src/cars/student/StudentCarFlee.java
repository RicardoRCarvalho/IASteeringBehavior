package cars.student;

import cars.engine.Car;
import cars.engine.Vector2;
import cars.engine.World;

import java.awt.*;

import static cars.engine.Vector2.vec2;

public class StudentCarFlee extends Car {
    public StudentCarFlee() {
        super(settings ->
          settings
            .color(Color.GREEN)
            .randomOrientation()
        );
    }


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
        double Sx = getPosition().x;
        double Sy = getPosition().y;
        Vector2 Sf = world.getClickPos();

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
    public Vector2 flee(final World world, double weight){
        double Sx = getPosition().x;
        double Sy = getPosition().y;
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
    @Override
    public Vector2 calculateSteering(final World world) {
        return flee(world, 1);
    }
}
