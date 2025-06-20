# FLUJO DE TRABAJO GIT/GITHUB CON MAIN, DEVELOP y RAMAS DE TRABAJO

## 1. 🌿 Estructura de ramas

- **`main:`** *Rama estable* (solo para releases finales).
- **`develop:`** *Rama principal de integración* (trabajo en progreso).
- **`Ramas de trabajo (features):`** *Se fusionan en `develop` cuando están completas*.
    - Ejemplo: `docs/create-WorkflowGitGithub`, `crud-user`  

## 2. 🛠️ Configuración inicial

### En Local:
```bash
    # Crear y cambiar a la rama develop
    git switch -c develop #git checkout -b develop

    # Subir develop a GitHub
    git push -u origin develop
```

### En GitHub:

    Si queremos hacerlo desde GitHub,
    Vamos al repositorio → "Branches" → Creamos develop desde main.

## 3. Crear una rama de característica (ej: docs/create-WorkflowGitGithub)
   
```bash
  # En local, nos asegúramos de estar en develop y de tener la rama actualizada
   git switch develop #git checkout develop
   git pull

  # Crearmos y cambiamos a la nueva rama
   git switch -c docs/create-WorkflowGitGithub #git checkout -b docs/create-WorkflowGitGithub

  ## Subimos la rama a GitHub
  git push -u origin docs/create-WorkflowGitGithub
```

## 4. Trabajar en la rama

```bash
  # Hacemos commits locales en docs/create-WorkflowGitGithub
  git add .
  git commit -m "Añadido menú principal"
  
  # Subimos los cambios a GitHub
  git push  
```

## 5. Fusionar (merge) la rama de trabajo en develop

### Opción 1: Desde GitHub (recomendado para colaboración)

Cuando generemos la PR no hacerlo desde el menú principal. 
Entrar dentro de las PR y generarla allí diciendo que queremos hacer el merge 
en develop. Si lo hacemos con el boton que nos sale al principio, haremos el 
merge contra main.

    1. Vamos a nuestro repositorio en GitHub.
    2. Creamos un Pull Request (PR) desde docs/create-WorkflowGitGithub hacia develop.
    3. Revisamos los cambios y hacemos clic en "Merge pull request".

### Opción 2: Desde local

```bash
  # Nos aseguramos de estar en develop
   git switch develop #git checkout develop
    
  # Fusionamos docs/create-WorkflowGitGithub en develop
  git merge docs/create-WorkflowGitGithub

  # Subir los cambios a GitHub
  git push origin develop
```

## 6. Eliminar la rama de trabajo (opcional)

### En local:

```bash
  # Elimina localmente
  git branch -d docs/create-WorkflowGitGithub
```

### En GitHub:

    Los branches se pueden eliminar desde el menú de "Branches" o después de fusionar un PR.

## 7. Release final (fusionar develop en main)

### Cuando develop esté estable y lista para producción:

```bash
    # Fusionar develop en main (local)
    git switch main #git checkout main
    git merge develop

    # Subir main a GitHub
    git push origin main
```

## 🔁 Resumen del flujo gráficamente

```text
    main    ----------------------------(merge final)----> v1.0
    ↗                        ↖
    develop   ⇢ (merge) ⇢ docs/create-WorkflowGitGithub     ⇢ (merge) ⇢ crud-user
```

## 📌 Comandos útiles


| **Acción**            | **Comando**                                                        |
|-----------------------|--------------------------------------------------------------------|
| Crear rama            | `git switch -c nombre-rama` o `git checkout -b nombre-rama` |
| Subir rama            | `git push -u origin nombre-rama`                                   |
| Resolver conflictos   | `git mergetool`                                                    |
| Cancelar merge        | `git merge --abort`                                                |

## 💡 Buenas prácticas

   ✔️ Nunca trabajar directamente en main o develop (usar ramas de trabajo).

   ✔️ Sincronizar develop frecuentemente para evitar conflictos:

```bash
    git switch develop #git checkout develop
    git pull origin develop
```
   ✔️ Usar Pull Requests (PRs) en GitHub para revisar cambios antes de fusionar.

   ✔️ Proteger main y develop en GitHub (requerir aprobación de PRs):

      Vamos a Settings → Branches → Add branch protection rule.

## ⚠️ Solución de problemas comunes

### Si hay conflictos al fusionar:

```bash
    git merge --abort  # Cancela el merge
    git stash          # Guarda cambios temporales
    git pull origin develop  # Actualiza develop
    git stash pop      # Recupera cambios y resuelve conflictos manualmente
```

### Si subiste cambios a main por error:

```bash
    git switch main #git checkout main
    git reset --hard origin/main  # Descarta cambios locales
```

## Ejemplo completo: Rama crud-user
 ```bash
    # Crear rama desde develop
    git switch develop #git checkout develop
    git pull
    git switch -c crud-user #git checkout -b crud-user
    git push -u origin crud-user

    # Trabajar y hacer commits
    git add .
    git commit -m "Añadido CRUD de usuarios"
    git push -u origin crud-user

    # Fusionar en develop (vía PR en GitHub o merge local)
    git switch develop #git checkout develop
    git merge crud-user
    git push origin develop
```

Con este flujo, mantendremos la rama main limpia y develop como la rama principal de trabajo.

---

# Porque usar git switch en lugar de de git checkout

## 1. Diferencias entre git checkout y git switch

### Ambos comandos permiten cambiar entre ramas, pero tienen enfoques distintos:

| **Característica**     | 	**git checkout (clásico)**                                      | **git switch (nuevo, desde Git 2.23)**        |
|------------------------|-----------------------------------------------|-----------------------------------------------|
| Propósito principal    | Cambiar ramas y restaurar archivos (uso mixto).                  | Solo para cambiar ramas (más seguro).         |      
| Restaurar archivos     | Sí (con git checkout -- archivo).                                | ❌ No. Usa git restore para esto.              |      
| Crear ramas            | Sí (git checkout -b nueva-rama).                                 | Sí (git switch -c nueva-rama).                |    
| Seguridad              | ❌ Permite acciones peligrosas (ej: descartar cambios sin stash). | ✅ Más seguro: evita acciones riesgosas.       |    
| Recomendación          | Legacy (aún funciona).	                                          | Usar este (más intuitivo y especializado).    |

## 2. 📌 Ejemplos clave

### 1. Cambiar a una rama existente
```bash
  # Con checkout (viejo)
  git checkout nombre-rama

  # Con switch (nuevo)
  git switch nombre-rama
```

### 2. Crear y cambiar a una rama nueva
```bash
  # Con checkout
  git checkout -b nueva-rama

  # Con switch
  git switch -c nueva-rama
```

### 3. Restaurar archivos (solo checkout)
```bash
  # Descarta cambios en un archivo (checkout)
  git checkout -- archivo.txt

  # Alternativa moderna (con restore)
  git restore archivo.txt
```

## 💡 ¿Cuál usar?

- Usamos git switch si nuestra versión de Git es ≥ 2.23:
  - Más claro para cambiar ramas.
  - Separa responsabilidades (evita confusiones).
- Usamos git checkout si:
  - Trabajamos con Git antiguo (< 2.23).
  - Necesitamos restaurar archivos (aunque git restore es mejor).

## 🔍 ¿Por qué se introdujo git switch?

Git dividió el comando checkout en dos para evitar errores:

- git switch: Solo para ramas.
- git restore: Solo para restaurar archivos.

Usándolo de esta forma evitamos hacer accidentalmente `git checkout -- archivo` cuando queríamos cambiar de rama.

