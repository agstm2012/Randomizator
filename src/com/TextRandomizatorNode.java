package com;

import org.omg.CORBA.Environment;

import javax.swing.text.Utilities;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TextRandomizatorNode {
    private TextRandomizatorNode parent = null;
    private String str = "";
    private String type = "mixing";
    private List<TextRandomizatorNode> subNodes = new ArrayList<TextRandomizatorNode>();
    private List<Integer> usedKeys = new ArrayList<Integer>();
    private String separator = "";
    private boolean isSeparator = false;

    public TextRandomizatorNode(TextRandomizatorNode parent) {
        this.parent = parent;
        if (parent != null)   //если parrent не пустой добавим его в subNodes.
        {
            parent.subNodes.add(this);
        }
    }

    public String getText() {
        String result = "";
        if (type.equals("synonyms")) {
            Random random = new Random();
            if (usedKeys.size() == 0) {      //добавляем порядковые элементы в usedKeys
                for (int i = 0; i < subNodes.size(); i++) {
                    usedKeys.add(i);
                }
            }
            int key = random.nextInt(usedKeys.size());   //получаем случайное значение
            result += subNodes.get(usedKeys.get(key)).getText(); //берем случайное значение
            usedKeys.remove(key); // и удаляем этот ключ потом
        } else if (type.equals("mixing")) {
            Collections.shuffle(subNodes);
            for (TextRandomizatorNode node : subNodes) {
                if (result.length() > 0)
                    result += " " + separator;
                result += " " + node.getText();
            }
        } else if (type.equals("series")) {
            for (TextRandomizatorNode node : subNodes) {
                result += " " + node.getText();
            }
        } else {
            result = str;
        }
        result = result.replace(" ,", ",")
                .replace(" .", ".")
                .replace(" !", "!")
                .replace(" ?", "?")
                .trim();
        return result;
    }

    public TextRandomizatorNode concat(String str) {
        if(isSeparator) {
            separator += str;
            return this;
        }
        if(type.equals("string")) {
            this.str += str;
            return this;
        }
        TextRandomizatorNode currentNode = new TextRandomizatorNode(this);
        currentNode.setType("string");
        return currentNode.concat(str);
    }

    public TextRandomizatorNode getParent() {
        return parent;
    }

    public void setParent(TextRandomizatorNode parent) {
        this.parent = parent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type.equals("string")) {
            this.type = "string";

        } else if (type.equals("synonyms")) {
            this.type = "synonyms";

        } else if (type.equals("series")) {
            this.type = "series";

        } else {
            this.type = "mixing";
        }
    }

    public boolean isSeparator() {
        return isSeparator;
    }

    public void setSeparator(boolean separator) {
        isSeparator = separator;
    }
}
