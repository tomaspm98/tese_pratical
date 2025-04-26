import sys
import math

def find_surface_area(radius):
    return 4 * math.pi * radius**2

def main():
    if len(sys.argv) != 2:
        print("Usage: python script.py <radius>")
        sys.exit(1)
    
    try:
        radius = int(sys.argv[1])
        surface_area = find_surface_area(radius)
        print(surface_area)
    except ValueError:
        print("Please provide a valid integer for the radius.")

if __name__ == "__main__":
    main()