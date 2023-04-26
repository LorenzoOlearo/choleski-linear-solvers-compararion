function mat = loadsparse(filename)
    mat = load(filename).Problem.A;
end