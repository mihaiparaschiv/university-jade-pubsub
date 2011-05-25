This project was developed during a course at Politehnica University of Bucharest. Authors: Mihai Paraschiv, Alexandru Stefanescu, Diana Adela Almasi.

Details about the project can be found in the attached [paper](https://github.com/mihaiparaschiv/tera-scpd/raw/master/paper.pdf):

> The publish/subscribe paradigm offers many advantages for communication in unstructured decentralized peer-to-peer networks. One of the models that fit into this paradigm is topic-based event dissemination. This paper presents an agent-based system for publish/subscribe inspired by TERA and implemented on top of JADE. We describe the system's design and implementation and show several performance results obtained in a real-life setting.

#### Code

The code was designed to evaluate the system's performance, as described in the paper.

There are two running configurations:
* run_main.bat: Loads a new JADE platform and creates an agent used for logging evaluation results.
* run_sec.bat: Creates a new batch of agents.

The project was imported from http://tera-scpd.googlecode.com/svn/JADEPubSub/.