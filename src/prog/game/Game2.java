/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prog.game;

import edu.lsu.cct.piraha.Grammar;
import edu.lsu.cct.piraha.Group;
import edu.lsu.cct.piraha.Matcher;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author sbrandt
 */
public class Game2 {

    private static boolean bothint(Number n1, Number n2) {
        return (n1 instanceof Integer) && (n2 instanceof Integer);
    }

    private static Number mul(Number n1, Number n2) {
        if (bothint(n1, n2)) {
            return n1.intValue() * n2.intValue();
        } else {
            return n1.doubleValue() * n2.doubleValue();
        }
    }

    private static Number add(Number n1, Number n2) {
        if (bothint(n1, n2)) {
            return n1.intValue() + n2.intValue();
        } else {
            return n1.doubleValue() + n2.doubleValue();
        }
    }
    
    private static Number sub(Number n1,Number n2) {
        if (bothint(n1, n2)) {
            return n1.intValue() - n2.intValue();
        } else {
            return n1.doubleValue() - n2.doubleValue();
        }
    }

    static class _Cell {

        int depth;
        int pos;
        Group gr;

        _Cell(int depth, int pos, Group gr) {
            this.depth = depth;
            this.pos = pos;
            this.gr = gr;
        }

        public String toString() {
            return "[p=" + pos + ":" + depth + ":" + gr.substring() + "]";
        }
    }

    static class _Block extends _Cell {

        List<_Cell> _body = new ArrayList<>();

        _Block(int depth, int pos, Group gr) {
            super(depth, pos, gr);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('{');
            for (_Cell c : _body) {
                sb.append(c);
            }
            sb.append('}');
            return sb.toString();
        }
    }

    static class _If extends _Block {

        List<_ElIf> options = new ArrayList<>();
        _Else _else;

        _If(int depth, int pos, Group gr) {
            super(depth, pos, gr);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("if(){");
            for (_Cell c : _body) {
                sb.append(c);
            }
            sb.append('}');
            for (_ElIf e : options) {
                sb.append(e);
            }
            if (_else != null) {
                sb.append(_else);
            }
            return sb.toString();
        }
    }

    static class _ElIf extends _Block {

        _ElIf(int depth, int pos, Group gr) {
            super(depth, pos, gr);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ElIf(){");
            for (_Cell c : _body) {
                sb.append(c);
            }
            sb.append('}');
            return sb.toString();
        }
    }

    static class _Else extends _Block {

        _Else(int depth, int pos, Group gr) {
            super(depth, pos, gr);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Else{");
            for (_Cell c : _body) {
                sb.append(c);
            }
            sb.append('}');
            return sb.toString();
        }
    }

    static class _While extends _Block {

        _While(int depth, int pos, Group gr) {
            super(depth, pos, gr);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("While(){");
            for (_Cell c : _body) {
                sb.append(c);
            }
            sb.append('}');
            return sb.toString();
        }
    }
    static List<_Cell> _main = new ArrayList<>();
    static List<_Cell> _by_pos = new ArrayList<>();
    public final static Random RAND = new Random();

    static class GameIter {

        LinkedList<_Cell> path = new LinkedList<>();

        public GameIter() {
            path.addAll(_main);
        }

        public boolean hasNext() {
            return path.size() > 0;
        }

        public _Cell next() {
            while (true) {
                _Cell c = path.removeFirst();
                if (c instanceof _If) {
                    _If _if = (_If) c;
                    if (beval(_if.gr)) {
                        addFront(path, _if._body);
                    } else {
                        boolean found = false;
                        for (_ElIf o : _if.options) {
                            if (beval(o.gr)) {
                                addFront(path, o._body);
                                found = true;
                                break;
                            }
                        }
                        if (!found && _if._else != null) {
                            addFront(path, _if._else._body);
                        }
                    }
                } else if (c instanceof _While) {
                    _While w = (_While) c;
                    if (beval(c.gr)) {
                        path.addFirst(c);
                        addFront(path, w._body);
                    }
                } else if (c instanceof _Block) {
                    _Block b = (_Block) c;
                    addFront(path, b._body);
                } else {
                    eval(c.gr);
                    return c;
                }
            }
        }

        private void addFront(LinkedList<_Cell> path, List<_Cell> _body) {
            for (int i = _body.size() - 1; i >= 0; i--) {
                path.addFirst(_body.get(i));
            }
        }
    }

    static Map<String, Number> values = new HashMap<>();

    static boolean beval(Group gr) {
        return (Boolean) eval(gr);
    }

    static Number neval(Group gr) {
        return (Number) eval(gr);
    }

    static Object eval(Group gr) {
        String n = gr.getPatternName();
        if ("line".equals(n)) {
            if (2 < gr.groupCount() && "condexpr".equals(gr.group(2).getPatternName())) {
                return eval(gr.group(2));
            } else if (1 < gr.groupCount() && "assign".equals(gr.group(1).getPatternName())) {
                return eval(gr.group(1));
            } else {
                return true;
            }
        } else if ("assign".equals(n)) {
            Number value = neval(gr.group(2));
            String aop = gr.group(1).substring();
            String key = gr.group(0).substring();
            if ("=".equals(aop)) {
                ;
            } else if ("+=".equals(aop)) {
                Number n2 = values.get(key);
                value = add(n2, value);
            } else if ("-=".equals(aop)) {
                Number n2 = values.get(key);
                value = sub(n2, value);
            } else {
                throw new Error(aop);
            }
            values.put(key, (Number) value);
            return values.get(key);
        } else if ("name".equals(n)) {
            String key = gr.substring();
            Number value = values.get(key);
            if (value == null) {
                throw new RuntimeException("Missing variable definition '" + key + "'");
            }
            return value;
        } else if ("num".equals(n)) {
            return Integer.valueOf(gr.substring());
        } else if ("addend".equals(n)) {
            Number n1 = neval(gr.group(0));
            for (int i = 2; i < gr.groupCount(); i += 2) {
                String op = gr.group(i - 1).substring();
                Number n2 = neval(gr.group(i));
                if ("*".equals(op)) {
                    n1 = mul(n1, n2);
                } else {
                    throw new Error();
                }
            }
            return n1;
        } else if ("expr".equals(n)) {
            Number n1 = neval(gr.group(0));
            for (int i = 2; i < gr.groupCount(); i += 2) {
                String op = gr.group(i - 1).substring();
                Number n2 = neval(gr.group(i));
                if ("+".equals(op)) {
                    n1 = add(n1, n2);
                } else {
                    throw new Error();
                }
            }
            return n1;
        } else if ("cond2".equals(n)) {
            Number n1 = neval(gr.group(0));
            String op = gr.group(1).substring();
            Number n2 = neval(gr.group(2));
            switch (op) {
                case "<":
                    return n1.doubleValue() < n2.doubleValue();
                case ">":
                    return n1.doubleValue() > n2.doubleValue();
                case "<=":
                    return n1.doubleValue() <= n2.doubleValue();
                case ">=":
                    return n1.doubleValue() >= n2.doubleValue();
                case "==":
                    return n1.doubleValue() == n2.doubleValue();
                case "!=":
                    return n1.doubleValue() != n2.doubleValue();
            }
            throw new Error();
        } else if ("condexpr".equals(n)) {
            if (gr.groupCount() == 3) {
                boolean b1 = beval(gr.group(0));
                String op = gr.group(1).substring();
                boolean b2 = beval(gr.group(2));
                if ("and".equals(op)) {
                    return b1 && b2;
                } else {
                    return b1 || b2;
                }
            } else {
                return eval(gr.group(0));
            }
        }
        gr.dumpMatches();
        throw new RuntimeException(n);
    }

    static Grammar g;

    static void init() throws IOException {
        if (g != null) {
            return;
        }
        g = new Grammar();
        InputStream in = Game2.class.getResourceAsStream("prog.peg");
        g.compileFile(in);
    }

    public static void main(String[] args) throws IOException {
        readGameFile("board.txt");

        System.out.println(_main);
        values.put("d1", 3);
        values.put("d2", 4);
        GameIter gi = new GameIter();
        while (gi.hasNext()) {
            _Cell c = gi.next();
            System.out.println(c.pos);
        }
    }

    /**
     *
     * @param fname
     * @throws FileNotFoundException
     */
    public static void readGameFile(String fname) throws IOException {
        System.out.println("fname="+fname);
        URL uri = Game2.class.getResource(fname);
        System.out.println("uri="+uri);
        InputStream in = uri.openStream();
        init();
        try (Scanner sc = new Scanner(in)) {
            int pos = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Matcher m = g.matcher("line", line);
                if (!m.matches()) {
                    System.out.println("line>>" + line);
                    System.out.println(m.near());
                }
                Group g1 = m.group(1);
                String n = g1.getPatternName();
                int depth = m.group(0).substring().length();
                String iew = "";
                if ("iew".equals(n)) {
                    iew = g1.substring();
                } else if ("x".equals(n)) {
                    iew = "x";
                } else if ("g".equals(n)) {
                    iew = "x";
                } else if ("assign".equals(n)) {
                    iew = "x";
                } else if ("else".equals(n)) {
                    iew = "else";
                }
                pos++;
                if ("if".equals(iew)) {
                    _main.add(new _If(depth, pos, m));
                } else if ("elif".equals(iew)) {
                    _main.add(new _ElIf(depth, pos, m));
                } else if ("else".equals(iew)) {
                    _main.add(new _Else(depth, pos, m));
                    System.out.println("else==>");
                    m.dumpMatches();
                } else if ("while".equals(iew)) {
                    _main.add(new _While(depth, pos, m));
                } else {
                    _main.add(new _Cell(depth, pos, m));
                }
            }
            _by_pos.addAll(_main);
            int index = _main.size() - 1;
            boolean done = false;
            while (!done) {
                done = true;
                while (index >= 0) {
                    if (_main.get(index) instanceof _ElIf && _main.get(index - 1) instanceof _If && _main.get(index).depth == _main.get(index - 1).depth) {
                        _If i = (_If) _main.get(index - 1);
                        _ElIf e = (_ElIf) _main.get(index);
                        i.options.add(e);
                        _main.remove(index);
                        done = false;
                    } else if (_main.get(index) instanceof _Else && _main.get(index - 1) instanceof _If && _main.get(index).depth == _main.get(index - 1).depth) {
                        _If i = (_If) _main.get(index - 1);
                        _Else e = (_Else) _main.get(index);
                        i._else = e;
                        _main.remove(index);
                        done = false;
                    } else if (_main.get(index).depth > 0 && _main.get(index).depth == 1 + _main.get(index - 1).depth && _main.get(index - 1) instanceof _Block) {
                        _Block b = (_Block) _main.get(index - 1);
                        b._body.add(_main.get(index));
                        _main.remove(index);
                        done = false;
                    } else {
                        index--;
                    }
                }
            }
        }
    }
}
