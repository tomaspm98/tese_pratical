import sys
import math

def is_Perfect_Square(n):
    if n < 0:
        return False
    root = int(math.sqrt(n))
    return root * root == n

def main():
    if len(sys.argv) != 2:
        print("Usage: python script.py <number>")
        sys.exit(1)
    
    number = int(sys.argv[1])
    result = is_Perfect_Square(number)
    print((result,)) if result else print(False)

if __name__ == "__main__":
    main()