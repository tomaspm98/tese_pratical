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
    
    number_str = sys.argv[1]
    try:
        number = int(number_str)
    except ValueError:
        print("Please provide a valid integer.")
        sys.exit(1)
    
    result = is_Perfect_Square(number)
    if result:
        print("True")
    else:
        print("False")

if __name__ == "__main__":
    main()