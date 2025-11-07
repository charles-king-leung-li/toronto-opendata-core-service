# Repository Rename Instructions

## 📝 What We've Updated

✅ **pom.xml**
- artifactId: `open-data-api` → `toronto-opendata-api`
- name: `Toronto Open Data Service` → `Toronto Open Data API`
- description: Updated to clarify it's a backend API for React Native

✅ **application.properties**
- spring.application.name: `open-data-api` → `toronto-opendata-api`

✅ **README.md**
- Created comprehensive documentation
- Describes architecture and relationship with frontend
- Includes setup instructions and API documentation

## 🔄 GitHub Repository Rename Steps

### 1. Rename on GitHub

1. Go to your repository on GitHub:
   ```
   https://github.com/charles-king-leung-li/dataserviceprovider
   ```

2. Click **Settings** (top right)

3. Scroll to **Repository name**

4. Change from:
   ```
   dataserviceprovider
   ```
   To:
   ```
   toronto-opendata-api
   ```

5. Click **Rename**

### 2. Update Your Local Repository

After renaming on GitHub, update your local git remote:

```bash
# Check current remote
git remote -v

# Update to new URL
git remote set-url origin https://github.com/charles-king-leung-li/toronto-opendata-api.git

# Verify the change
git remote -v
```

### 3. Commit Current Changes

```bash
# Stage all changes
git add .

# Commit
git commit -m "Rename project to toronto-opendata-api and add comprehensive documentation"

# Push to GitHub
git push origin main
```

## 📁 Optional: Rename Local Project Folder

If you want to rename your local folder to match:

**Windows (PowerShell):**
```powershell
# Navigate to parent directory
cd C:\Users\Charles\IdeaProjects\TorontoOpenDataProviderService

# Rename folder (close IDE first!)
Rename-Item -Path "dataserviceprovider" -NewName "toronto-opendata-api"
```

**Then reopen the project** in your IDE from the new location.

## 🎯 Final Structure

After all changes:

```
Repository URL:
https://github.com/charles-king-leung-li/toronto-opendata-api

Local Path:
C:\Users\Charles\IdeaProjects\TorontoOpenDataProviderService\toronto-opendata-api

Project Name (pom.xml):
toronto-opendata-api

Application Name:
toronto-opendata-api
```

## ✅ Verification Checklist

After renaming:

- [ ] GitHub repository renamed to `toronto-opendata-api`
- [ ] Local git remote updated
- [ ] Changes committed and pushed
- [ ] Application still runs: `./mvnw spring-boot:run`
- [ ] Swagger UI accessible: http://localhost:8080/swagger-ui/index.html
- [ ] All endpoints working

## 🔗 Update These Later

When you create your React Native frontend:

1. **In Frontend README**, link back:
   ```markdown
   Backend API: https://github.com/charles-king-leung-li/toronto-opendata-api
   ```

2. **In This README**, update line 246:
   ```markdown
   For the frontend React Native application, see [your-frontend-repo]
   ```

## 💡 Tips

- **Don't rename the parent folder** `TorontoOpenDataProviderService` yet - it won't break anything
- **Update your IDE** workspace/project settings if needed
- **GitHub will redirect** old URLs to new ones automatically for a while
- **Update any bookmarks** or links you have saved

---

**Ready to proceed?** Follow the steps above in order!
