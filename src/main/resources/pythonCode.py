import sys
import math

def is_Perfect_Square(n):
    root = int(math.sqrt(n))
    return root * root == n

def main():
    if len(sys.argv) != 2:
        print("Usage: python script.py <number>")
        sys.exit(1)
    
    number = int(sys.argv[1])
    result = is_Perfect_Square(number)
    print(result)

if __name__ == "__main__":
    main()