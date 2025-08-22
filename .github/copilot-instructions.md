# Scalameta Repository Instructions

Scalameta is a library to process Scala code as data, offering syntactic APIs (tokenizers, parsers, trees, quasiquotes) and semantic APIs (SemanticDB schema, compiler plugins, and tools). This repository contains libraries, compiler plugins, and command-line tools for Scala 2.11, 2.12, 2.13, and Scala 3.

**Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.**

## Working Effectively

### Prerequisites and Setup
- Install Java 8, 11, or 17 (preferred: Java 17)
- Install SBT (Scala Build Tool) - use version 1.10+ from https://www.scala-sbt.org/
- If needed, install Yarn for website development: https://yarnpkg.com/

### Bootstrap and Build Process
**NEVER CANCEL builds or tests - they may take 30-60 minutes. Set timeouts to 90+ minutes.**

1. **Initial compilation** (first-time setup):
   ```bash
   sbt
   # Wait for SBT to initialize (~15 seconds)
   # Exit SBT and then run specific builds
   ```

2. **Core compilation** (~25-50 seconds):
   ```bash
   sbt commonJVM/compile          # Compile common utilities
   sbt scalametaJVM/compile       # Compile main scalameta library
   sbt testsJVM/compile          # Compile tests (~50 seconds)
   ```

3. **SemanticDB compilation** (~45-60 seconds, expect many warnings):
   ```bash
   sbt testsSemanticdb/compile   # NEVER CANCEL - takes 45+ seconds
   ```

### Testing
**CRITICAL: All test commands take 2-15 minutes. NEVER CANCEL. Set timeout to 30+ minutes.**

- **Main parser tests** (~2-5 minutes):
  ```bash
  sbt testsJVM/test             # NEVER CANCEL - runs all parser tests
  ```

- **JavaScript tests** (~2-3 minutes):
  ```bash
  sbt testsJS/test              # NEVER CANCEL - cross-platform JS tests
  ```

- **Native tests** (~3-5 minutes):
  ```bash
  sbt testsNative/test          # NEVER CANCEL - cross-platform Native tests
  ```

- **SemanticDB tests** (~3-15 minutes):
  ```bash
  sbt testsSemanticdb/test      # NEVER CANCEL - semantic analysis tests
  ```

- **Target specific test suites** (faster, ~30 seconds - 2 minutes):
  ```bash
  sbt 'testsJVM/testOnly scala.meta.tests.parsers.ParseSuite'
  sbt 'testsJVM/testOnly *ParseSuite'  # Use quotes for glob patterns
  ```

- **Community integration tests** (~1-2 minutes):
  ```bash
  sbt communitytest/test        # NEVER CANCEL
  ```

### Code Quality and Formatting
- **Format code** (~20 seconds):
  ```bash
  ./bin/scalafmt               # Format all code
  ./bin/scalafmt --test        # Check formatting (CI requirement)
  ```

- **Check binary compatibility**:
  ```bash
  sbt mima                     # Runs MiMa binary compatibility checks
  ```

### Cross-Platform and Multi-Version Testing
The repository supports multiple Scala versions and platforms. Key versions:
- **Scala 2.11.12** (oldest supported)
- **Scala 2.12.16-2.12.20** 
- **Scala 2.13.13-2.13.16**
- **Scala 3.3.6, 3.7.2**

Test across versions:
```bash
sbt ++2.13.16 testsJVM/test   # Test specific Scala version
sbt ++2.12.20 testsJVM/test   # Test older Scala version
```

## Website Development
The website uses Docusaurus with mdoc preprocessing:

1. **Preprocess markdown** (continuous):
   ```bash
   sbt
   docs/mdoc -w                 # Watch and regenerate docs
   # Keep this running in one terminal
   ```

2. **Run website** (in another terminal):
   ```bash
   cd website
   yarn install                 # First time only (~60 seconds)
   yarn start                   # Start development server
   # Visit http://localhost:3000
   ```

## Critical Validation Steps

### Before Committing Changes
**ALWAYS run these in order - do not skip any step:**

1. **Format code**:
   ```bash
   ./bin/scalafmt              # REQUIRED - CI will fail without this
   ```

2. **Basic compilation**:
   ```bash
   sbt testsJVM/compile        # Must complete successfully
   ```

3. **Run targeted tests**:
   ```bash
   sbt 'testsJVM/testOnly *YourChangedArea*'  # Target your changes
   ```

4. **Full format check**:
   ```bash
   ./bin/scalafmt --test       # CI requirement - must pass
   ```

### For SemanticDB Changes
If modifying anything in `semanticdb/` directory:
```bash
sbt testsSemanticdb/compile  # NEVER CANCEL - takes 45+ seconds
sbt testsSemanticdb/test     # NEVER CANCEL - takes 5-15 minutes
```

## Key Projects and Structure

### Repository Layout
- `scalameta/` - Syntactic APIs: tokenizers, parsers, trees, quasiquotes, pretty printers
- `semanticdb/` - Semantic APIs: SemanticDB schema, compiler plugins, metacp, metap  
- `tests/` - Unit and integration tests for scalameta APIs
- `tests-semanticdb/` - SemanticDB-specific tests
- `website/` - Documentation website (Docusaurus + mdoc)
- `docs/` - Markdown documentation source files
- `bin/` - Utility scripts (scalafmt, etc.)

### Important SBT Projects
- `commonJVM` - Common utilities and base classes
- `scalametaJVM` - Main scalameta library for JVM
- `parsersJVM` - Scala parser implementation
- `testsJVM` - Main test suite
- `testsSemanticdb` - SemanticDB tests
- `semanticdbScalacPlugin` - Scala compiler plugin
- `semanticdbMetacp` - Classpath processor
- `semanticdbMetap` - SemanticDB pretty printer

## Common Troubleshooting

### Build Issues
- **"Welcome to scalameta" message**: This is normal, indicates successful SBT initialization
- **Protobuf compilation errors**: Run `sbt clean` and retry - protobuf files are generated automatically
- **Out of memory**: Check `.jvmopts` file is present with memory settings
- **Permission errors on ./bin/scalafmt**: Run `chmod +x ./bin/scalafmt`

### Test Issues  
- **Tests timing out**: Increase timeout - builds and tests legitimately take 2-15 minutes
- **Deprecation warnings in SemanticDB**: Normal and expected, especially in integration tests
- **"No tests found" for testOnly**: Ensure proper quoting: `'testsJVM/testOnly *SuiteName*'`

### Website Issues
- **mdoc not found**: Run `sbt docs/mdoc` first to generate preprocessed files
- **Yarn install fails**: Check network connectivity, try `yarn install --network-timeout 60000`
- **Port 3000 in use**: Use `yarn start --port 3001` for alternative port

## Special SBT Commands

The repository defines several custom SBT commands:

- `save-expect` - Update expected test outputs (for expectation-based tests)
- `save-manifest` - Update test manifest files  
- `download-scala-library` - Download Scala standard library source for tests
- `releaseSemanticdb` - Publish SemanticDB artifacts
- `benchAll`, `benchLSP`, `benchQuick` - Performance benchmarking

## Build Time Expectations

Set appropriate timeouts and **NEVER CANCEL** these operations:

| Operation | Expected Time | Timeout Setting |
|-----------|---------------|-----------------|
| SBT initialization | 10-15 seconds | 30 seconds |
| Basic compile | 25-50 seconds | 90 seconds |
| testsJVM/test | 2-5 minutes | 10 minutes |
| testsSemanticdb/test | 5-15 minutes | 30 minutes |
| All cross-platform tests | 15-30 minutes | 60 minutes |
| scalafmt --test | 20 seconds | 60 seconds |
| Website yarn install | 60 seconds | 3 minutes |

## Development Workflow Summary

1. **Start development**:
   ```bash
   sbt                          # Initialize (15s)
   # Make your changes
   ./bin/scalafmt              # Format code
   sbt testsJVM/compile        # Quick validation (30s)
   ```

2. **Test changes** (choose appropriate scope):
   ```bash
   sbt 'testsJVM/testOnly *RelevantSuite*'  # Target specific tests (30s-2min)
   # OR for semantic changes:
   sbt testsSemanticdb/test    # Full semantic tests (5-15min)
   ```

3. **Pre-commit validation**:
   ```bash
   ./bin/scalafmt --test       # Required for CI
   sbt testsJVM/test          # Full parser tests (2-5min)
   ```

**Remember**: This is a complex metaprogramming library. Builds are legitimately slow due to code generation, cross-compilation, and comprehensive testing. Never cancel long-running operations.