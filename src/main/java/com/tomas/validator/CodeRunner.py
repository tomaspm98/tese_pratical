import sys

def add_integers(a, b):
    return a + b

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python CodeRunner.py <int1> <int2>")
        sys.exit(1)

    try:
        num1 = int(sys.argv[1])
        num2 = int(sys.argv[2])
        result = add_integers(num1, num2)
        print(result)
    except ValueError:
        print("Invalid input: Please provide two integers.")
        sys.exit(1)