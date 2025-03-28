import sys

def subtract_integers(a, b):
    return a - b

def main():
    a = int(sys.argv[1])
    b = int(sys.argv[2])
    result = subtract_integers(a, b)
    print(result)

if __name__ == "__main__":
    main()