import sys
import math

def is_perfect_square(n):
    sqrt_n = int(math.sqrt(n))
    return sqrt_n * sqrt_n == n

def main():
    if len(sys.argv) != 2:
        print("Usage: python script.py <number>")
        sys.exit(1)
    
    number = int(sys.argv[1])
    result = is_perfect_square(number)
    print(result)

if __name__ == "__main__":
    main()