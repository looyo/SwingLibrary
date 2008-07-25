/*
 * Copyright 2008 Nokia Siemens Networks Oyj
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.robotframework.swing.tree;

import javax.swing.JPopupMenu;
import javax.swing.tree.TreePath;

import org.netbeans.jemmy.EventTool;
import org.netbeans.jemmy.operators.JPopupMenuOperator;
import org.robotframework.swing.context.Context;
import org.robotframework.swing.factory.IdentifierParsingOperatorFactory;

/**
 * @author Sami Honkonen
 * @author Heikki Hulkko
 */
public class TreePopupMenuOperatorFactory extends IdentifierParsingOperatorFactory<JPopupMenuOperator> {
    @Override
    public JPopupMenuOperator createOperatorByIndex(int index) {
        JPopupMenu popupMenu = treeContext().callPopupOnRow(index);
        return createPopupOperator(popupMenu);
    }

    @Override
    public JPopupMenuOperator createOperatorByName(String path) {
        TreePath treePath = treeContext().findPath(path);
        JPopupMenu popupMenu = treeContext().callPopupOnPath(treePath);
        return createPopupOperator(popupMenu);
    }

    JPopupMenuOperator createPopupOperator(JPopupMenu popupMenu) {
        JPopupMenuOperator popupMenuOperator = new JPopupMenuOperator(popupMenu);
        popupMenuOperator.grabFocus();
        waitToAvoidInstability();
        return popupMenuOperator;
    }

    private void waitToAvoidInstability() {
        new EventTool().waitNoEvent(500);
    }

    private EnhancedTreeOperator treeContext() {
        return (EnhancedTreeOperator) Context.getContext();
    }
}
