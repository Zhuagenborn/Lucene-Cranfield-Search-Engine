# *Lucene Cranfield* Search Engine

[![Java](docs/badges/Java.svg)](https://www.java.com)
[![Maven](docs/badges/Made-with-Maven.svg)](https://maven.apache.org)
![License](docs/badges/License-MIT.svg)

## Introduction

![Cover](Cover.jpg)

A [***Lucene***](https://lucene.apache.org/) demo for searching the *Cranfield* collection. It searches for each query in file `cran.qry` and displays the first two related documents in file `cran.all.1400`.

```console
The results for the query with ID 27: papers on shock-sound wave interaction .
    SCORE: 7.306487, DOCUMENT ID: 64, TITLE: unsteady oblique interaction of a shock wave with plane disturbances .
    SCORE: 6.157679, DOCUMENT ID: 291, TITLE: sweepback effects in the turbulent boundary-layer shock-wave interaction .

The results for the query with ID 29: material properties of photoelastic materials .
    SCORE: 4.421124, DOCUMENT ID: 405, TITLE: tables of thermal properties of gases .
    SCORE: 4.305917, DOCUMENT ID: 553, TITLE: ablation of glassy materials around blunt bodies of revolution .
```

The workflow is as follows.

![workflow](docs/workflow.png)

## Getting Started

### Prerequisites

- Install [*Java*](https://www.java.com).
- Install [*Maven*](https://maven.apache.org).

### Building

```bash
mvn package
```

## Class Diagram

```mermaid
classDiagram

namespace Lucene {
    class LuceneAnalyzer
    class LuceneSimilarity
    class LuceneScoreDoc
    class LuceneDocument
    class LuceneIndexSearcher
}

namespace Cran {
    class CranField {
        Field id$
        Field title$
        Field author$
        Field bibliography$
        Field words$

        String tag
        String name
    }

    class CranDocument {
        int id
        String title
        String author
        String bibliography
        String words
    }

    class CranParser {
        parseDocuments(text)$ List~CranDocument~
        parseQueries(text)$ List~CranQuery~
    }

    class CranQuery {
        int id
        String words
    }
}

CranParser ..> CranDocument
CranParser ..> CranQuery

class Indexer {
    index(List~CranDocument~, path)
}

Indexer --> LuceneAnalyzer
Indexer --> LuceneSimilarity
Indexer ..> CranField
Indexer ..> CranDocument

class Searcher {
    search(CranQuery, top) List~LuceneScoreDoc~
    documents(List~LuceneScoreDoc~) List~LuceneDocument~
    document(LuceneScoreDoc) LuceneDocument
}

Searcher --> LuceneIndexSearcher
Searcher --> LuceneAnalyzer
Searcher ..> LuceneScoreDoc
Searcher ..> LuceneDocument
Searcher ..> CranField
Searcher ..> CranQuery
```

## License

Distributed under the *MIT License*. See `LICENSE` for more information.