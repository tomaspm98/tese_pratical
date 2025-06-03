import sys

def first_repeated_char(str1):
    seen = set()
    for char in str1:
        if char in seen:
            return (True, char)
        seen.add(char)
    return (False, None)

def main():
    if len(sys.argv) != 2:
        print("Usage: python script.py <string>")
        sys.exit(1)
    
    str1 = sys.argv[1]
    result = first_repeated_char(str1)
    print(result)

if __name__ == "__main__":
    main()