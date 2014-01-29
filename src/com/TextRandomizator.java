package com;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextRandomizator {
    private String text = "";
    private TextRandomizatorNode tree = new TextRandomizatorNode(null);

    public String getText() {
        return tree.getText();
    }

    public void setText(String text) {
        this.text = text;
    }

    public TextRandomizator(String text) {
        this.text = text;
        //String preg = @"\\\\|\\\+|\\\{|\\\}|\\\[|\\\]|\\\||\\|\[\+|\+|\{|\}|\[|\]|\||[^\\\+\{\}\|\[\]]+";
        String preg = "\\\\\\\\|\\\\\\+|\\\\\\{|\\\\\\}|\\\\\\[|\\\\\\]|\\\\\\||\\\\|\\[\\+|\\+|\\{|\\}|\\[|\\]|\\||[^\\\\\\+\\{\\}\\|\\[\\]]+";
        TextRandomizatorNode currentNode = this.tree;
        currentNode = new TextRandomizatorNode(currentNode);
        currentNode.setType("series");
        currentNode = currentNode.concat("");
        TextRandomizatorNode node;

        Pattern p = Pattern.compile(preg);
        Matcher m = p.matcher(text);

        while (m.find()) {
            String s = m.group();
            if (s.equals("\\\\") || s.equals("\\")) {
                currentNode = currentNode.concat("\\");

            } else if (s.equals("\\+")) {
                currentNode = currentNode.concat("+");

            } else if (s.equals("\\{")) {
                currentNode = currentNode.concat("{");

            } else if (s.equals("\\}")) {
                currentNode = currentNode.concat("}");

            } else if (s.equals("\\[")) {
                currentNode = currentNode.concat("[");

            } else if (s.equals("\\]")) {
                currentNode = currentNode.concat("]");

            } else if (s.equals("\\|")) {
                currentNode = currentNode.concat("|");

            } else if (s.equals("[+")) {
                if ("string" == currentNode.getType()) {
                    currentNode = new TextRandomizatorNode(currentNode.getParent());
                } else {
                    currentNode = new TextRandomizatorNode(currentNode);
                }
                currentNode.setSeparator(true);

            } else if (s.equals("+")) {
                if (currentNode.isSeparator()) {
                    currentNode.setSeparator(false);
                    currentNode = new TextRandomizatorNode(currentNode);
                    currentNode.setType("series");
                    currentNode = currentNode.concat("");
                } else {
                    currentNode = currentNode.concat("+");
                }

            } else if (s.equals("{")) {
                if ("string" == currentNode.getType()) {
                    currentNode = new TextRandomizatorNode(currentNode.getParent());
                } else {
                    currentNode = new TextRandomizatorNode(currentNode);
                }
                currentNode.setType("synonyms");
                currentNode = new TextRandomizatorNode(currentNode);
                currentNode.setType("series");
                currentNode = currentNode.concat("");

            } else if (s.equals("}")) {
                node = currentNode.getParent().getParent();
                if (node != null && "synonyms".equals(node.getType())) {
                    currentNode = node.getParent();
                    currentNode = currentNode.concat("");
                } else {
                    currentNode = currentNode.concat("}");
                }

            } else if (s.equals("[")) {
                if ("string" == currentNode.getType()) {
                    currentNode = new TextRandomizatorNode(currentNode.getParent());
                } else {
                    currentNode = new TextRandomizatorNode(currentNode);
                }
                currentNode = new TextRandomizatorNode(currentNode);
                currentNode.setType("series");
                currentNode = currentNode.concat("");

            } else if (s.equals("]")) {
                node = currentNode.getParent().getParent();
                if (node != null && "mixing".equals(node.getType()) && node.getParent() != null) {
                    currentNode = node.getParent();
                    currentNode = currentNode.concat("");
                } else {
                    currentNode = currentNode.concat("]");
                }

            } else if (s.equals("|")) {
                node = currentNode.getParent();
                if (node != null && "series".equals(node.getType())) {
                    currentNode = node.getParent();
                    currentNode = new TextRandomizatorNode(currentNode);
                    currentNode.setType("series");
                    currentNode = currentNode.concat("");
                } else {
                    currentNode = currentNode.concat("|");
                }

            } else {
                currentNode = currentNode.concat(m.group());
            }
        }

    }


    public static void main(String[] args) {
        TextRandomizator textRandomizator = new TextRandomizator("{{Рандомизатор|Рандомайзер} {|текста} - {|это}\n" +
                "|{Рандомизатором|Рандомайзером} {|текста} называется\n" +
                "}\n" +
                "{{|спамерский|оптимизаторский|SEO|сео|сеошный|рерайтерский}\n" +
                " {инструмент|скрипт|софт}\n" +
                " {|, предназначенный|, который {предназначен|нужен|используется}}\n" +
                "|{|спамерская|оптимизаторская|SEO|сео|сеошная|рерайтерская}\n" +
                " {программа|прога|софтина|утилита}\n" +
                " {|, предназначенная|, которая предназначена|, которая нужна}\n" +
                "} для\n" +
                "{{|промышленного|индустриального|поточного|массового}\n" +
                " {{рерайтинга|рерайта}\n" +
                "  {\n" +
                "  |{|псевдоуникальных|уникальных|новых|рандомных} {статей|публикаций|текстов}\n" +
                "  |{|псевдоуникального|уникального|нового|рандомного} контента\n" +
                "  }\n" +
                " |{переписывания|производства|создания|клепания|размножения|написания}\n" +
                "  {{|псевдоуникальных|уникальных|новых|рандомных} {статей|публикаций|текстов}\n" +
                "  |{|псевдоуникального|уникального|нового|рандомного} контента\n" +
                "  }\n" +
                " }\n" +
                "|{|промышленной|индустриальной|поточной|массовой}\n" +
                " {уникализации|псевдоуникализации|рандомизации}\n" +
                " {статей|контента|публикаций|текстов}\n" +
                "}\n" +
                "{\n" +
                "|в целях\n" +
                " {{|поискового} спама\n" +
                " |{|поисковой|SEO|сео|сеошной} оптимизации {|контента|сайта}\n" +
                " |{|поискового|SEO|сео|сеошного} продвижения {|сайта}\n" +
                " |продвижения {|сайта} в {поисковой выдаче|выдаче поисковиков}\n" +
                " }\n" +
                "|для нужд\n" +
                " {{|поисковой|SEO|сео|сеошной} оптимизации {|контента|сайта}\n" +
                " |{|поискового|SEO|сео|сеошного} продвижения {|сайта}\n" +
                " |продвижения {|сайта} в {поисковой выдаче|выдаче поисковиков}\n" +
                " |{|SEO|сео|сеошных|поисковых} {оптимизаторов|продвиженцев}\n" +
                " }\n" +
                "}.");
        System.out.println(textRandomizator.getText());

    }
}

