import sys

def multiply(a, b):
    result = 0
    sign = -1 if (a < 0) ^ (b < 0) else 1
    a, b = abs(a), abs(b)
    for _ in range(b):
        result += a
    return sign * result

def main():
    if len(sys.argv) != 3:
        print("Usage: python script.py <int1> <int2>")
        sys.exit(1)
    
    try:
        int1 = int(sys.argv[1])
        int2 = int(sys.argv[2])
        product = multiply(int1, int2)
        print(product)
    except ValueError:
        print("Both inputs must be integers.")
        sys.exit(1)

if __name__ == "__main__":
    main()