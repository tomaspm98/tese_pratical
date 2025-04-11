import sys

def has_opposite_sign(a, b):
    return (a ^ b) < 0

def main():
    # Read input from command line arguments
    a = int(sys.argv[1])
    b = int(sys.argv[2])
    
    result = has_opposite_sign(a, b)
    print(result)

if __name__ == "__main__":
    main()