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

package org.robotframework.swing.keyword.tree;

import junit.framework.Assert;

import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.robotframework.swing.tree.TreeSupport;

/**
 * @author Heikki Hulkko
 */
@RobotKeywords
public class TreeNodeVisibilityKeywords extends TreeSupport {
    @RobotKeyword("Fails if the tree node is not visible.\n"
        + "Assumes current context is a tree.\n\n"
        + "Example:\n"
        + "| Tree Node Should Be Visible | _Root|Folder_ |\n")
    public void treeNodeShouldBeVisible(String nodePath) {
        verifyContextAndPath(nodePath);

        Assert.assertTrue("Tree node '" + nodePath + "' is not visible.", isVisible(nodePath));
    }

    @RobotKeyword("Fails if the tree node is visible.\n"
        + "Assumes current context is a tree.\n\n"
        + "Example:\n"
        + "| Tree Node Should Not Be Visible | _Root|Folder_ |\n")
    public void treeNodeShouldNotBeVisible(String nodePath) {
        verifyContextAndPath(nodePath);

        Assert.assertFalse("Tree node '" + nodePath + "' is visible.", isVisible(nodePath));
    }

    private boolean isVisible(String nodePath) {
        return treeOperator().isVisible(treeOperator().findPath(nodePath));
    }

    private void verifyContextAndPath(String nodePath) {
        verifyContext();
        verifyPath(nodePath);
    }

    private void verifyPath(String nodePath) {
        if (isIndex(nodePath))
            throw new IllegalArgumentException("Node's visibility cannot be checked by it's index by it's index");
    }
}
