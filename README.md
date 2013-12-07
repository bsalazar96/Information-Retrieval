<h1>Information Retrieval - CS371R</h1>

<h3>University of Texas at Austin </h3>

<p>About:
  Various aspects of a retrieval system, implemented for class credits</p>

<ol>
<li> Vector Space Retrieval (vsr)</li>
  <p>Perform retrieval based on the cosine similarities of documents
  <br />
  Invoke with:
  java ir.vsr.InvertedIndex  -html [path to document corpus]</p>
<li>Relevance Feedback</li>
  <p>Reassess retrievals with relevance feedback and pseudofeedback
  <br />
  Invoke with:
  java ir.vsr.InvertedIndex -html -pseudofeedback 8 [path to document corpus]</p>

<li>PageRank</li>
  <p>Implemented PageRank to spider a corpus of documents and perform link analysis. 
   <br />
  Invoke with: 
  java ir.vsr.PageRankInvertedIndex -html -weight 0 crawledPages</p>

<li>Text Categorization</li>
  <p>Implemented Rocchio and KNN algorithms to categorize a set of sample documents. 
    <br />
  Invoke with:
  java ir.classifiers.TestKNN [-K K]
  java ir.classifiers.TestRocchio [-neg]</p>
