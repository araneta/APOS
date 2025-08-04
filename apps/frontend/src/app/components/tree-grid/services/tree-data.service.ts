import { Injectable } from '@angular/core';
import { FlatTreeRow, TreeNode, FlatNode } from '../interfaces/tree-grid.interface';

@Injectable({
  providedIn: 'root'
})
export class TreeDataService {

  buildTreeData(data: FlatTreeRow[]): TreeNode[] {
    // Convert flat data to tree
    const idMap = new Map<string | number, TreeNode>();
    const roots: TreeNode[] = [];
    
    for (const row of data) {
      idMap.set(row.id, { ...row, children: [], isExpanded: false });
    }
    
    for (const node of idMap.values()) {
      if (node.parentId == null) {
        roots.push(node);
      } else {
        const parent = idMap.get(node.parentId);
        if (parent) {
          parent.children = parent.children || [];
          parent.children.push(node);
        }
      }
    }
    
    this.processTreeData(roots);
    return roots;
  }

  processTreeData(nodes: TreeNode[], parentId?: string | number, level: number = 0): void {
    nodes.forEach(node => {
      node.level = level;
      node.parentId = parentId ?? null;
      node.isLeaf = !node.children || node.children.length === 0;
      node.hasChildren = !node.isLeaf;
      if (node.hasChildren && node.isExpanded === undefined) {
        node.isExpanded = false;
      }
      if (node.children) {
        this.processTreeData(node.children, node.id, level + 1);
      }
    });
  }

  flattenTree(nodes: TreeNode[]): FlatNode[] {
    const result: FlatNode[] = [];
    const traverse = (nodes: TreeNode[]) => {
      nodes.forEach(node => {
        result.push({
          ...node,
          level: node.level || 0,
          isLeaf: node.isLeaf || false,
          hasChildren: node.hasChildren || false
        });
        if (node.children && node.children.length > 0 && node.isExpanded === true) {
          traverse(node.children);
        }
      });
    };
    traverse(nodes);
    return result;
  }

  findNodeById(nodes: TreeNode[], id: string | number): TreeNode | null {
    for (const node of nodes) {
      if (node.id === id) return node;
      if (node.children) {
        const found = this.findNodeById(node.children, id);
        if (found) return found;
      }
    }
    return null;
  }
}
