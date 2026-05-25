const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');

try {
  const status = execSync('git status --porcelain', { encoding: 'utf8' });
  const files = [];
  const moves = [];

  status.split('\n').forEach(line => {
    if (!line.trim()) return;

    // git status --porcelain output format:
    // XY file
    // R  old -> new
    const code = line.substring(0, 2);
    const rest = line.substring(3).trim();

    if (code.startsWith('R') || code.includes('R')) {
      const parts = rest.split(' -> ');
      if (parts.length === 2) {
        moves.push({ oldPath: parts[0], newPath: parts[1] });
        files.push(parts[1]);
      }
    } else {
      const filePath = rest;
      if (fs.existsSync(filePath) && fs.lstatSync(filePath).isFile()) {
        if (filePath !== 'replicate.js' && filePath !== 'pack.js' && filePath !== 'changes.patch' && !filePath.includes('grep.exe.stackdump') && !filePath.includes('.log')) {
          files.push(filePath);
        }
      }
    }
  });

  const fileMap = {};
  files.forEach(file => {
    if (fs.existsSync(file)) {
      fileMap[file] = fs.readFileSync(file, 'utf8');
    }
  });

  const scriptContent = `
const fs = require('fs');
const path = require('path');

// ==========================================
// CONFIGURATIONS FOR YOUR WORK LAPTOP
// ==========================================
// Set this to the name of your backend directory (e.g. 'ILP-Project-main')
const TARGET_BACKEND_FOLDER = 'ILP-Project-main'; 

// Set this to the name of your frontend directory (e.g. 'frontend')
const TARGET_FRONTEND_FOLDER = 'frontend';       

// Set to true if you want to relocate files in your backend folder into a subfolder named 'backend'
// (Highly recommended so that the backend paths and configurations align with the new workspace structure)
const RELOCATE_BACKEND_TO_SUBFOLDER = true; 
// ==========================================

console.log('Starting migration script...');
console.log('Target Backend Folder: ' + TARGET_BACKEND_FOLDER);
console.log('Target Frontend Folder: ' + TARGET_FRONTEND_FOLDER);

// Helper function to resolve dynamic paths on your target machine
function resolvePath(originalPath) {
  if (originalPath.startsWith('backend/')) {
    const subpath = originalPath.substring('backend/'.length);
    if (RELOCATE_BACKEND_TO_SUBFOLDER) {
      return path.join(TARGET_BACKEND_FOLDER, 'backend', subpath);
    } else {
      return path.join(TARGET_BACKEND_FOLDER, subpath);
    }
  } else if (originalPath.startsWith('frontend/')) {
    const subpath = originalPath.substring('frontend/'.length);
    return path.join(TARGET_FRONTEND_FOLDER, subpath);
  }
  return originalPath;
}

// 1. Move old files if they are being relocated into a subfolder
if (RELOCATE_BACKEND_TO_SUBFOLDER) {
  const moves = ${JSON.stringify(moves, null, 2)};
  moves.forEach(m => {
    // Determine old path relative to the backend folder (e.g., ILP-Project-main/src/...)
    // Since original m.oldPath is like 'src/main/...', we join it with the backend folder name
    const actualOldPath = path.join(TARGET_BACKEND_FOLDER, m.oldPath);
    const actualNewPath = resolvePath(m.newPath);

    if (fs.existsSync(actualOldPath)) {
      const parent = path.dirname(actualNewPath);
      if (!fs.existsSync(parent)) {
        fs.mkdirSync(parent, { recursive: true });
      }
      try {
        fs.renameSync(actualOldPath, actualNewPath);
        console.log('Moved ' + actualOldPath + ' to ' + actualNewPath);
      } catch (e) {
        console.error('Could not rename ' + actualOldPath + ': ' + e.message);
      }
    }
  });
}

// 2. Write updated files
const fileMap = ${JSON.stringify(fileMap, null, 2)};

Object.entries(fileMap).forEach(([filePath, content]) => {
  const actualPath = resolvePath(filePath);
  const dir = path.dirname(actualPath);
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir, { recursive: true });
  }
  fs.writeFileSync(actualPath, content, 'utf8');
  console.log('Created/Updated: ' + actualPath);
});

console.log('\\nAll changes successfully applied on your work laptop!');
`;

  fs.writeFileSync('replicate.js', scriptContent, 'utf8');
  console.log('Generated replicate.js successfully!');
} catch (e) {
  console.error('Error generating replicate.js:', e);
}
