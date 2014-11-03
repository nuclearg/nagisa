package com.github.nuclearg.nagisa.lang.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.nuclearg.nagisa.lang.lexer.LexToken;
import com.github.nuclearg.nagisa.lang.parser.rule.NullRule;
import com.github.nuclearg.nagisa.lang.parser.rule.SyntaxRule;
import com.github.nuclearg.nagisa.lang.util.Range;

/**
 * 璇硶鏍戣妭鐐�
 * 
 * @author ng
 *
 */
public class SyntaxTreeNode {
    /**
     * 瀵瑰簲鐨勮娉曞厓绱�
     */
    public final SyntaxRule rule;
    /**
     * 瀵瑰簲鐨勮娉曞厓绱犵殑鍚嶇О
     */
    public final String ruleName;
    /**
     * 瀵瑰簲鐨勮瘝娉曞厓绱�
     */
    public final List<LexToken> tokens;
    /**
     * 瀛愬厓绱狅紝鍙兘涓簄ull
     */
    public final List<SyntaxTreeNode> children;
    /**
     * 鍦ㄦ簮鏂囦欢涓殑浣嶇疆
     */
    public final Range range;

    public SyntaxTreeNode(SyntaxRule rule, LexToken token) {
        this.rule = rule;
        this.ruleName = null;
        this.tokens = Arrays.asList(token);
        this.children = null;
        this.range = token.range;
    }

    public SyntaxTreeNode(SyntaxRule rule, List<SyntaxTreeNode> children) {
        this.rule = rule;
        this.ruleName = null;
        this.tokens = children.stream().map(e -> e.tokens).reduce(Collections.emptyList(), (a, b) -> {
            List<LexToken> list = new ArrayList<>();
            list.addAll(a);
            list.addAll(b);
            return list;
        });
        this.children = Collections.unmodifiableList(children);

        if (!this.children.isEmpty()) {
            SyntaxTreeNode first = children.get(0);
            SyntaxTreeNode last = children.get(children.size() - 1);
            this.range = new Range(first.range.startRow, first.range.startColumn, last.range.endRow, last.range.endColumn);
        } else
            this.range = new Range(0, 0, 0, 0);
    }

    public SyntaxTreeNode(NullRule rule, Range range) {
        this.rule = rule;
        this.ruleName = null;
        this.tokens = Collections.emptyList();
        this.children = null;
        this.range = range;
    }

    public SyntaxTreeNode(String name, SyntaxTreeNode node) {
        this.rule = node.rule;
        this.ruleName = name;
        this.tokens = node.tokens;
        this.children = node.children;
        this.range = node.range;
    }

    @Override
    public String toString() {
        return StringUtils.join(this.tokens.stream().map(e -> e.text).toArray(), " ");
    }
}
