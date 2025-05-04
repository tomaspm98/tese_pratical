method CylinderVolume(radius: real, height: real) returns (volume: real)
    requires radius > 0.0 && height > 0.0
    ensures volume == 3.14159265359 * radius * radius * height
{
    volume := 3.14159265359 * radius * radius * height;
}