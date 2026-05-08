import { Suspense, useMemo, useRef } from "react";
import { Canvas, useFrame } from "@react-three/fiber";
import { OrbitControls, Line } from "@react-three/drei";
import * as THREE from "three";

interface Props {
  vector: { x: number; y: number; z: number };
  trail: Array<{ x: number; y: number; z: number }>;
  accent: string;
}

function Sphere() {
  return (
    <mesh>
      <sphereGeometry args={[1, 14, 10]} />
      <meshBasicMaterial color="#ffffff" transparent opacity={0.05} wireframe />
    </mesh>
  );
}

function Frame() {
  // Three great circles (xy / xz / yz planes in math coordinates).
  // Math (x,y,z) maps to three (x, z, y) so |0> sits at +Y on screen.
  const N = 128;
  const circles = useMemo(() => {
    const equator: [number, number, number][] = [];   // math z = 0
    const meridXZ: [number, number, number][] = [];   // math y = 0
    const meridYZ: [number, number, number][] = [];   // math x = 0
    for (let i = 0; i <= N; i++) {
      const t = (i / N) * Math.PI * 2;
      const c = Math.cos(t), s = Math.sin(t);
      equator.push([c, 0, s]);
      meridXZ.push([c, s, 0]);
      meridYZ.push([0, c, s]);
    }
    return { equator, meridXZ, meridYZ };
  }, []);

  // Small tick marks at ±0.5 on every axis to give a sense of scale.
  const ticks = useMemo(() => {
    const t = 0.025;
    const at: Array<[[number, number, number], [number, number, number]]> = [
      [[ 0.5, -t, 0], [ 0.5,  t, 0]],
      [[-0.5, -t, 0], [-0.5,  t, 0]],
      [[-t,  0.5, 0], [ t,  0.5, 0]],
      [[-t, -0.5, 0], [ t, -0.5, 0]],
      [[0, -t,  0.5], [0,  t,  0.5]],
      [[0, -t, -0.5], [0,  t, -0.5]],
    ];
    return at;
  }, []);

  const axisCol = "rgba(255,255,255,0.32)";
  const circleCol = "#ffffff";
  return (
    <>
      <Line points={[[-0.99, 0, 0], [0.99, 0, 0]]} color={axisCol} lineWidth={1} />
      <Line points={[[0, -0.99, 0], [0, 0.99, 0]]} color={axisCol} lineWidth={1} />
      <Line points={[[0, 0, -0.99], [0, 0, 0.99]]} color={axisCol} lineWidth={1} />
      {ticks.map((seg, i) => (
        <Line key={i} points={seg} color={axisCol} lineWidth={1} />
      ))}
      <Line points={circles.equator} color={circleCol} transparent opacity={0.18} lineWidth={1} />
      <Line points={circles.meridXZ} color={circleCol} transparent opacity={0.10} lineWidth={1} />
      <Line points={circles.meridYZ} color={circleCol} transparent opacity={0.10} lineWidth={1} />
    </>
  );
}

function BlochArrow({
  vector,
  accent,
}: {
  vector: { x: number; y: number; z: number };
  accent: string;
}) {
  const HEAD_LEN = 0.11;
  const HEAD_WIDTH = 0.04;
  const SHAFT_RADIUS = 0.013;
  const arrowColor = useMemo(
    () => new THREE.Color(accent).lerp(new THREE.Color("#ffffff"), 0.08),
    [accent],
  );

  const groupRef = useRef<THREE.Group>(null);
  const shaftRef = useRef<THREE.Mesh>(null);
  const coneRef = useRef<THREE.Mesh>(null);
  const current = useRef(new THREE.Vector3(0, 0, 0));
  const tmpQuat = useMemo(() => new THREE.Quaternion(), []);
  const yAxis = useMemo(() => new THREE.Vector3(0, 1, 0), []);

  useFrame((_, dt) => {
    const target = new THREE.Vector3(vector.x, vector.z, vector.y);
    current.current.lerp(target, Math.min(1, dt * 5));
    const len = Math.max(current.current.length(), 1e-3);
    const dir =
      len > 1e-6 ? current.current.clone().multiplyScalar(1 / len) : yAxis;
    if (groupRef.current) {
      tmpQuat.setFromUnitVectors(yAxis, dir);
      groupRef.current.quaternion.copy(tmpQuat);
    }
    const shaftLen = Math.max(len - HEAD_LEN, 0.001);
    if (shaftRef.current) {
      shaftRef.current.scale.y = shaftLen;
      shaftRef.current.position.y = shaftLen / 2;
    }
    if (coneRef.current) {
      coneRef.current.position.y = shaftLen + HEAD_LEN / 2;
    }
  });

  return (
    <group ref={groupRef}>
      <mesh ref={shaftRef}>
        <cylinderGeometry args={[SHAFT_RADIUS, SHAFT_RADIUS, 1, 24]} />
        <meshLambertMaterial color={arrowColor} />
      </mesh>
      <mesh ref={coneRef}>
        <coneGeometry args={[HEAD_WIDTH, HEAD_LEN, 28]} />
        <meshLambertMaterial color={arrowColor} />
      </mesh>
    </group>
  );
}

function Trail({
  trail,
  accent,
}: {
  trail: Array<{ x: number; y: number; z: number }>;
  accent: string;
}) {
  if (trail.length < 2) return null;
  const points = trail.map((p) => [p.x, p.z, p.y] as [number, number, number]);
  // Constant brightness along the whole trail so it reads as a fat ribbon,
  // not a fading streak that only appears at the ball.
  return <Line points={points} color={accent} lineWidth={2.5} transparent opacity={0.4} />;
}

export function BlochSphere({ vector, trail, accent }: Props) {
  return (
    <Canvas
      camera={{ position: [2.4, 1.6, 2.4], fov: 45 }}
      gl={{ antialias: true, alpha: true }}
      style={{ background: "transparent" }}
    >
      <ambientLight intensity={0.55} />
      <directionalLight position={[3, 4, 5]} intensity={0.8} />
      <directionalLight position={[-3, -2, -2]} intensity={0.25} />
      <Suspense fallback={null}>
        <Sphere />
        <Frame />
        <Trail trail={trail} accent={accent} />
        <BlochArrow vector={vector} accent={accent} />
      </Suspense>
      <OrbitControls enablePan={false} minDistance={2} maxDistance={6} />
    </Canvas>
  );
}
